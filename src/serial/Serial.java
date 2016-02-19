package serial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Serial {
	
	static File temp;
	
	public Serial() throws SerialException
	{	
		try {
			temp = File.createTempFile("serialization", ".temp");
			if( ! new File(temp.getAbsolutePath()).isFile())
				throw new SerialException("Couldn't create temp file!");
			temp.setReadable(true);
			temp.setReadable(true);
			temp.setWritable(true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SerialException("Couldn't start Serial");
		}
	}
	
	public String getPath()
	{
		return null;
	}
	
	public String readTemp() throws IOException
	{
		BufferedReader read = new BufferedReader(new FileReader(temp));
		StringBuilder files = new StringBuilder();
		String ten[] = {null, System.getProperty("line.seperator")};
		
		while( (ten[0] = read.readLine()) != null)
		{
			files.append(ten[0]);
			files.append(ten[1]);
		}
		read.close();
		return files.toString().replace("null", "");
	}
	
	public String encryptTemp(String username, String password, String fileDir) throws Exception
	{
			File input = new File(fileDir);
			BufferedReader read = new BufferedReader(new FileReader(input));
			StringBuilder files = new StringBuilder();
			String ten[] = {null, System.getProperty("line.seperator")};
			
			while( (ten[0] = read.readLine()) != null)
			{
				files.append(ten[0]);
				files.append(ten[1]);
			}
			read.close();
			String full = files.toString().replace("null", "");
			FileWriter file = new FileWriter(temp);
			System.out.println("Saved: " + temp.getAbsolutePath());
			//Encrypt enc = new Encrypt();
			//enc.FileEncrypter(password, fileDirs, temp.getAbsolutePath());
			//String encrypted = enc.encrypter(username, password, full);
			file.write(full);
			file.close();
			return temp.getAbsolutePath();
	}
	
	public String decryptTemp(String username, String password) throws IOException
	{
		return this.decryptTemp(username, password, null);
	}
	
	public String decryptTemp(String username, String password, String toTemps) throws IOException {
		String full;
		if(toTemps == null)
		{
			BufferedReader read = new BufferedReader(new FileReader(temp));
			StringBuilder files = new StringBuilder();
			String ten[] = {null, System.getProperty("line.seperator")};
			
			while( (ten[0] = read.readLine()) != null)
			{
				files.append(ten[0]);
				files.append(ten[1]);
			}
			read.close();
			full = files.toString().replace(" ", "").replace("\n", "").replace("null", "");
		}
		else
		{
			full = toTemps;
		}
		//Encrypt enc = new Encrypt();
		//String decrypted = enc.decrypter(username, password, full);
		return full;
	}
}

class Encrypt {
	
	public String encrypter(String key, String vector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
	
	public void FileEncrypter(String password, File inputFile, String path) throws Exception
	{
		FileInputStream inFile = new FileInputStream(inputFile);
		FileOutputStream outFile = new FileOutputStream(path);
		byte[] salt = new byte[8];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt);
		File temps = File.createTempFile("saltycrap", ".enc");
		File tempsiv = File.createTempFile("ivs", ".enc");
		FileOutputStream saltOutFile = new FileOutputStream(temps);
		saltOutFile.write(salt);
		saltOutFile.close();
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keySpec = new PBEKeySpec("javapapers".toCharArray(), salt, 65536,
				256);
		SecretKey secretKey = factory.generateSecret(keySpec);
		SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();
		FileOutputStream ivOutFile = new FileOutputStream(tempsiv);
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		ivOutFile.write(iv);
		ivOutFile.close();
		byte[] input = new byte[64];
		int bytesRead;
		while ((bytesRead = inFile.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, bytesRead);
			if (output != null)
				outFile.write(output);
		}
		byte[] output = cipher.doFinal();
		if (output != null)
			outFile.write(output);
		inFile.close();
		outFile.flush();
		outFile.close();
	}
	
    public String decrypter(String key, String vector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(value));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
