package com.thefirstlineofcode.basalt.protocol.rbupdater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		Main main = new Main();
		main.run(args);
	}
	
	public void run(String[] args) {
		if (args.length < 2) {
			printUsage();
			return;
		}
		
		File[] files = new File[args.length - 1];
		for (int i = 0; i < args.length - 1; i++) {
			File file = getBxmppPropertiesFile(args[i]);
			if (file == null) {
				System.out.println("Can't get BXMPP properties file. " + args[i] + " isn't a BXMPP properties file or project directory.");
				printUsage();
				return;
			}
			
			files[i] = file;
		}
		
		if (!args[args.length - 1].startsWith("0x")) {
			System.out.println(args[args.length - 1] + " isn't a valid replacement bytes.");
			printUsage();
			return;
		}
		
		byte[] bytes = null;
		try {
			bytes = toBinaryBytes(args[args.length - 1]);
		} catch (NumberFormatException e) {
			System.out.println(args[1] + " isn't a valid replacement bytes.");
			printUsage();
			return;
		}
		
		if (bytes.length > 2) {
			System.out.println(args[1] + " isn't a vaild replacement bytes.");
			printUsage();
			return;
		}
		
		if (bytes.length == 2 && ((bytes[0] & 0xff) < 0xf0) || ((bytes[0] & 0xff) > 0xf9)) {
			System.out.println(args[1] + " isn't a valid replacement bytes.");
			printUsage();
			return;
		}
		
		byte[] currentBytes = bytes;
		for (File file : files) {			
			currentBytes = updateReplacementBytes(file, currentBytes);
		}
	}
	
	private File getBxmppPropertiesFile(String sFileOrDirectory) {
		File fileOrDirectory = new File(sFileOrDirectory);
		
		if (!fileOrDirectory.exists()) {
			System.out.println(sFileOrDirectory + " isn't a file or directory.");
		}
		
		if (fileOrDirectory.isFile())
			return fileOrDirectory;
		
		if (fileOrDirectory.isDirectory()) {
			String sResourcesDirectory = sFileOrDirectory + "/src/main/resources";
			
			File bxmppCore = new File(sResourcesDirectory + "/META-INF/bxmpp-core.properties");
			if (bxmppCore.exists())
				return bxmppCore;
			
			File bxmppIm = new File(sResourcesDirectory + "/META-INF/bxmpp-im.properties");
			if (bxmppIm.exists())
				return bxmppIm;
			
			File bxmppExtension = new File(sResourcesDirectory + "/META-INF/bxmpp-extension.properties");
			if (bxmppExtension.exists())
				return bxmppExtension;
		}
		
		System.out.println("BXMPP properties file not found. Path of properties file or project directory is " + sFileOrDirectory);
		
		return null;
	}

	private byte[] updateReplacementBytes(File sourceFile, byte[] start) {
		System.out.println("Try to update replacement bytes of " + sourceFile);
		
		File updateFile = new File(sourceFile.getPath() + ".update");
		if (updateFile.exists() && !updateFile.delete()) {
			throw new RuntimeException("Can't delete old update file. Please delete it manually.");
		}
		
		try {
			if (!updateFile.createNewFile()) {
				throw new RuntimeException("Can't create update file.");
			}
		} catch (IOException e) {
			throw new RuntimeException("Can't create update file.", e);
		}
		
		int secondByte = start.length == 2 ? start[1] : start[0];
		int iCurrentSecondByte = secondByte & 0xff;
		BufferedReader sourceFileReader = null;
		BufferedWriter updateFileWriter = null;
		try {
			sourceFileReader = new BufferedReader(new FileReader(sourceFile));
			updateFileWriter = new BufferedWriter(new FileWriter(updateFile));
			
			String line = sourceFileReader.readLine();	
			String lineSeparator = System.getProperty("line.separator");
			
			while (line != null) {
				if (line.isEmpty() || line.startsWith("#") || (start.length == 1 && iCurrentSecondByte == 0xf0) ||
						(start.length == 2 && iCurrentSecondByte == 0xff)) {
					updateFileWriter.write(line + lineSeparator);
					line = sourceFileReader.readLine();
					continue;
				}
				
				line = line.trim() + lineSeparator;
				if (line.startsWith("0x") && line.indexOf("=") != -1) {
					int equalMark = line.indexOf("=");
					if (equalMark != -1) {
						line = line.substring(equalMark + 1);
					}
				}
				
				String newLine;
				if (start.length == 2) {
					newLine = String.format("0x%02x0x%02x=%s", start[0] & 0xff, iCurrentSecondByte, line);						
				} else {
					newLine = String.format("0x%02x=%s", iCurrentSecondByte, line);						
				}
				
				updateFileWriter.write(newLine);
				
				if (iCurrentSecondByte != 0xf0)
					iCurrentSecondByte++;
				
				line = sourceFileReader.readLine();
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found.", e);
		} catch (IOException e) {
			throw new RuntimeException("Can't read file.", e);
		} finally {
			if (sourceFileReader != null) {
				try {
					sourceFileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (updateFileWriter != null) {
				try {
					updateFileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (!sourceFile.delete()) {
			throw new RuntimeException("Can't delete old source file.");
		}
		
		if (!updateFile.renameTo(sourceFile)) {
			throw new RuntimeException("Can't rename update file to source file.");
		}
		
		System.out.println("Replacement bytes of " + sourceFile + " updated.");
		
		if (start.length == 1) {
			return new byte[] {(byte)iCurrentSecondByte};
		}
		
		return new byte[] {start[0], (byte)iCurrentSecondByte};
	}
	
	private byte[] toBinaryBytes(String bytesString) {
		String[] arrayByteStrings = bytesString.split("0x");
		
		List<String> byteStrings = new ArrayList<>();
		for (String byteString : arrayByteStrings) {
			if (!byteString.isEmpty())
				byteStrings.add(byteString);
		}
		
		byte[] bytes = new byte[byteStrings.size()];
		for (int i = 0; i < bytes.length; i++) {
			int num = Integer.decode("0x" + byteStrings.get(i));
			bytes[i] = (byte)num;
		}
		
		return bytes;
	}
	
	private void printUsage() {
		System.out.println("Usage:");
		System.out.println("java -jar com.thefirstlineofcode.basalt.rbupdater-${VERSION}.jar BXMPP-FILES START-REPLACEMENT-BYTES.");
	}
}
