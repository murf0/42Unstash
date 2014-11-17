package se.murf.unstash;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Unstash {
	private static String stashFile;
	private byte[] bytes = new byte[0];

	private void readfile(String filename) {
		try {

			File file = new File(filename);

			if (!file.isFile()) {

				System.err.println("cannot read " + filename);

				throw new IOException("cannot read file");
			}

			// File length
			int size = (int) file.length();
			if (size > Integer.MAX_VALUE) {
				System.err.println("Filesize is too large.");
			}
			bytes = new byte[size];
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			int read = 0;
			int numRead = 0;
			while (read < bytes.length
					&& (numRead = dis.read(bytes, read, bytes.length - read)) >= 0) {
				read = read + numRead;
			}
			// System.out.println("File size: " + read);
			// Ensure all the bytes have been read in
			if (read < bytes.length) {
				System.err.println("Could not completely read: "
						+ file.getName());
			}
			dis.close();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private void unstash() {
		byte x = (byte) 0xf5; // 11110101
		for (int i = 0; i < bytes.length; i++) {
			byte b = (byte) (bytes[i] ^ x);

			if (b == 0)
				break; // continue until 0 found
			//System.err.print(b + " ");
			System.out.print((char) b);
		}
		System.out.println();
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("\nunstash IBM keyDB passwords, version 1.0.1");
			System.out.println("\nby Jeroen Zomer (jeroen.zomer@axxius.nl) (2009)");
			System.out.println("based on  Ben Laurie's (in)famous PERL script (1999)");
			stashFile="key.sth";
		} else {
			stashFile=args[0];
		}
		Unstash worker = new Unstash();
		worker.readfile(stashFile);
		worker.unstash();
	}

}
