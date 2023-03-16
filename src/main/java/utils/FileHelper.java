package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class FileHelper {
	public static void main(String [] args) throws FileNotFoundException {
//		HashMap<String,Integer> reposMap = getValidReposMap("/home1/michael/BadMethodName/debug-method-name-master/Data/TestData/TestMethodInfo.txt");
////		System.out.println(reposMap.size());
//		MapSorter sorter = new MapSorter();
//		Map<String,Integer> reposMap1 = sorter.sortByValueDescending(reposMap);
////		for(Map.Entry<String,Integer> entry:reposMap1.entrySet()){
////			System.out.println(entry.getKey()+":"+entry.getValue());
////		}
//		List<String> inValidReposList=null;
//		//deserialize object
//		try {
////			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(basePath + "methodInfoAndBodyMap.txt"));
//			ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream( "/tmp/inValidReposList.txt"));
//			inValidReposList = (List<String>) ois1.readObject();
////			success1000MethodInfoAndBodyMap = (HashMap<String, String>) ois1.readObject();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		List<String> allRepos = FileHelper.readFileByLines("/home1/michael/BadMethodName/JavaRepos/repos.txt");
//		HashMap<String,Integer> candidateReposMap = new HashMap<>();
//		for(Map.Entry<String,Integer> entry:reposMap1.entrySet() ){
//			if(!inValidReposList.contains(entry.getKey())&&allRepos.contains(entry.getKey())){
//				candidateReposMap.put(entry.getKey(),entry.getValue());
//			}
//		}
//		Map<String,Integer> candidateReposMap1 = sorter.sortByValueDescending(candidateReposMap);
//		for(Map.Entry<String,Integer> entry:candidateReposMap1.entrySet()){
//			System.out.println(entry.getKey()+":"+entry.getValue());
//		}
//		System.out.println(candidateReposMap.size());
//		List<String> candidateList = new ArrayList<>(candidateReposMap1.keySet()).subList(0,42);
//		System.out.println(candidateList);
//
//
//		System.out.println(allRepos.size());
//		Boolean isRemove = allRepos.removeAll(candidateList);
//		System.out.println(isRemove);
//		System.out.println(allRepos.size());
//		System.out.println(allRepos);
	}
	
	public static String getRepositoryName(String gitRepositoryPath) {
		// ../../git/commons-math/.git
		String gitRepositoryName = getFileName(getFileParentPath(gitRepositoryPath));

		return gitRepositoryName;
	}
	public static HashMap<String,Integer> getValidReposMap(String inconsistentResultFile) throws FileNotFoundException {
		ArrayList<String> resultLines = readFileByLines(inconsistentResultFile);
		HashMap<String,Integer> reposMap = new HashMap<>();
		for(String s:resultLines){
			String repoName = s.substring(0,s.indexOf(":"));
			if(reposMap.get(repoName)!=null){
				int num = reposMap.get(repoName);
				reposMap.put(repoName,num+1);
			}
			else{
				reposMap.put(repoName,1);
			}
		}

		return reposMap;
	}

	/**
	 * 
	 * @param filePath
	 */
	public static void createDirectory(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			deleteDirectory(filePath);
		}
		file.mkdirs();
	}
	
	public static void createFile(File file, String content) {
		FileWriter writer = null;
		BufferedWriter bw = null;

		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) file.createNewFile();
			writer = new FileWriter(file);
			bw = new BufferedWriter(writer);
			bw.write(content);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(bw);
			close(writer);
		}
	}
	
	public static void deleteDirectory(String dir) {
		File file = new File(dir);
		
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files.length > 0) {
					for (File f : files) {
						if (f.isFile()) {
							deleteFile(f.getAbsolutePath());
						} else {
							deleteDirectory(f.getAbsolutePath());
						}
					}
				}
				file.delete();
			} else {
				deleteFile(dir);
			}
		}
	}
	
	public static void deleteFiles(String dir) {
		File file = new File(dir);
		
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files.length > 0) {
					for (File f : files) {
						if (f.isFile()) {
							deleteFile(f.getAbsolutePath());
						} else {
							deleteFiles(f.getAbsolutePath());
						}
					}
				}
			} else {
				deleteFile(dir);
			}
		}
	}
	
	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else {
				deleteDirectory(fileName);
			}
		} 
	}
	
	public static List<File> getAllDirectories(String filePath) {
		return listAllDirectories(new File(filePath));
	}
	
	public static List<File> getAllDirectories1(String filePath) {
		return listAllDirectories1(new File(filePath));
	}
	
	/**
	 * List all files in the directory.
	 * 
	 * @param filePath
	 * @param type
	 * @return
	 */
	public static List<File> getAllFiles(String filePath, String type) {
		return listAllFiles(new File(filePath), type);
	}
	
	public static List<File> getAllFilesInCurrentDiectory(String filePath, String type) {
		return getAllFilesInCurrentDiectory(new File(filePath), type);
	}
	
	public static List<File> getAllFilesInCurrentDiectory(File directory, String type) {
		List<File> fileList = new ArrayList<>();
		
		if (!directory.exists()) {
			return null;
		}
		
		File[] files = directory.listFiles();
		
		for (File file : files) {
			if (file.isFile()) {
				if (file.toString().endsWith(type)) {
					fileList.add(file);
				}
			} 
		}
		
		return fileList;
	}
	
	public static String getFileName(String filePath) {
		File file = new File(filePath);
		
		if (file.exists()) {
			return file.getName();
		} else {
			return null;
		}
	}
	
	public static String getFileNameWithoutExtension(File file) {
		if (file.exists()) {
			String fileName = file.getName();
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
			return fileName;
		} else {
			return null;
		}
	}

	public static String getFileExtension(File file) {
		String fileName = file.getName();
		String extension = fileName.substring(fileName.lastIndexOf("."));
		return extension;
	}
	
	public static String getFileParentPath(String filePath) {
		File file = new File(filePath);
		
		if (file.exists()) {
			return file.getParent() + "/";
		}
		return "";
	}
	
	/**
	 * Check whether a file path is valid or not.
	 * 
	 * @param path, file path.
	 * @return true, the file path is valid.
	 * 		   false, the file path is invalid.
	 */
	public static boolean isValidPath(String path) {
		File file = new File(path);
		
		if (file.exists()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Recursively list all files in file.
	 * 
	 * @param file
	 * @return
	 */
	private static List<File> listAllFiles(File file, String type) {
		List<File> fileList = new ArrayList<>();
		
		if (!file.exists()) {
			return null;
		}
		
		File[] files = file.listFiles();
		
		for (File f : files) {
			if (f.isFile()) {
				if (f.toString().endsWith(type)) {
					fileList.add(f);
				}
			} else {
				List<File> fl = listAllFiles(f, type);
				if (fl != null && fl.size() > 0) {
					fileList.addAll(fl);
				}
			}
		}
		
		return fileList;
	}
	
	/**
	 * Recursively list all directories in file.
	 * 
	 * @param file
	 * @return
	 */
	private static List<File> listAllDirectories(File file) {
		List<File> fileList = new ArrayList<>();
		
		File[] files = file.listFiles();
		
		for (File f : files) {
			if (f.isDirectory()) {
				fileList.add(f);
				fileList.addAll(listAllDirectories(f));
			} 
		}
		
		return fileList;
	}
	
	private static List<File> listAllDirectories1(File file) {
		List<File> fileList = new ArrayList<>();
		
		File[] files = file.listFiles();
		
		for (File f : files) {
			if (f.isDirectory()) {
				fileList.add(f);
//				fileList.addAll(listAllDirectories(f));
			} 
		}
		
		return fileList;
	}
	
	public static void makeDirectory(String fileName) {
		deleteFile(fileName);
		File file = new File(fileName).getParentFile();
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * Read the content of a file.
	 * 
	 * @param fileName
	 * @return String, the content of a file.
	 */
	public static String readFile(String fileName) {
		return readFile(new File(fileName));
	}
	
	/**
	 * Read the content of a file.
	 * 
	 * @param file
	 * @return String, the content of a file.
	 */
	public static String readFile(File file) {
		byte[] input = null;
		BufferedInputStream bis = null;
		
		try {
			
			bis = new BufferedInputStream(new FileInputStream(file));
			input = new byte[bis.available()];
			bis.read(input);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(bis);
		}
		
		String sourceCode = null;
		if (input != null) {
			sourceCode = new String(input);
		}
		
		return sourceCode;
	}
	public static ArrayList<String> readFileByLines(String filePath) throws FileNotFoundException {
		FileInputStream fileInputStream1 = new FileInputStream(filePath);
		Scanner scanner1 = new Scanner(fileInputStream1);
		ArrayList<String> list = new ArrayList<>();
		while(scanner1.hasNext()){
			String line = scanner1.nextLine();
			list.add(line);
		}
		return list;
	}
	/**
	 * Output output into a file.
	 * @param fileName, output file name.
	 * @param data, output data.
	 * @param append, the output data will be appended previous data in the file or not.
	 */
	public static void outputToFile(String fileName, StringBuilder data, boolean append) {
		outputToFile(fileName, data.toString(), append);
	}
	
	public static void outputToFile(File file, StringBuilder data, boolean append) {
		outputToFile(file, data.toString(), append);
	}
	
	public static void outputToFile(String fileName, String data, boolean append) {
		File file = new File(fileName);
		outputToFile(file, data, append);
	}
	
	public static void outputToFile(File file, String data, boolean append) {
		FileWriter writer = null;
		BufferedWriter bw = null;

		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(file, append);
			bw = new BufferedWriter(writer);
			bw.write(data);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(bw);
			close(writer);
		}
	}

	private static void close(FileWriter writer) {
		try {
			if (writer != null) {
				writer.close();
				writer = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void close(BufferedWriter bw) {
		try {
			if (bw != null) {
				bw.close();
				bw = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void close(BufferedInputStream bis) {
		try {
			if (bis != null) {
				bis.close();
				bis = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<File> getAllSubDirectories(String fileName) {
		File file = new File(fileName);
		List<File> subDirectories = new ArrayList<>();
		if (file.exists()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					subDirectories.add(f);
				}
			}
		}
		return subDirectories;
	}
	
}
