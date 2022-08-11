package com.gali.util;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closer;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * @author 颜伟凡
 */
public class FileUtils {

	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * read a file to a string
	 */
	public static String readFile(String filePath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} catch (Exception e) {
			logger.error("!!!", e);
		} finally {
			CloseUtils.close(br);
		}

		return null;
	}

	/**
	 * read a file to a string
	 */
	public static String readFile(File file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} catch (Exception e) {
			logger.error("!!!", e);
		} finally {
			CloseUtils.close(br);
		}

		return null;
	}



	/**
	 * Function name:saveChatTxt Description: 将字符集合写入文件
	 * @param chats :
	 * @param path :
	 * @return
	 */
	public static boolean saveChatTxt(List<String> chats, String path) {
		if (chats == null || chats.size() < 1) {
			return true;
		}
		boolean ok = false;
		File fo = null;
		FileOutputStream to = null;
		PrintWriter out = null;
		try {
			fo = new File(path);
			if (fo.exists()) { // 文件存在,先删除
				fo.delete();
			}

			fo.createNewFile();// 创建新文件

			to = new FileOutputStream(fo, false); // 创建文件输出流
			out = new PrintWriter(to); // 输出流

			for (String msg : chats) {
				out.println(msg);
			}
			// out.println("------------------------导出成功,共:" + chats.size() +
			// "条记录.");
			ok = true;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		} catch (NullPointerException ee) {
			ee.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				out.close(); // 关闭流
				to.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	// 删除文件夹
	// param folderPath 文件夹完整绝对路径
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 把文件按行读入并返回list
	 * @param filePath
	 * @return
	 */
	public static List<String> readFileReturnList(String filePath) {
		List<String> rs = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();

			while (line != null) {
				rs.add(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			logger.error("!!!", e);
		} finally {
			CloseUtils.close(br);
		}

		return rs;
	}
	
	 /**
     * @param path
     * @param data
     * @param append:是否追加到文件末尾
     * @return
     */
    public static boolean saveBytes(String path,byte[] data,boolean append){
    	File ff = new File(path);
    	if(ff.exists()){
    		System.out.println("------------警告,覆盖文件:" + path);
    	}
        boolean r = false;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path, append);
            out.write(data);
            r = true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return r;
    }
    
    /**
     * 加载文件,返回字节数组
     * @param path
     * @return
     */
	public static byte[] loadBytes(String path) {
		byte[] msgData = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(path);
			if (in != null && in.available() > 0) {
				msgData = new byte[in.available()];
				in.read(msgData);
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return msgData;
	}

	public static void downLoad(String url, File downFile) {
		Closer closer = Closer.create();
		try {
			InputStream in = closer.register(new URL(url).openStream());
			Files.write(ByteStreams.toByteArray(in), downFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				closer.close();
			} catch (IOException e) {
				closer = null;
			}
		}
	}

}
