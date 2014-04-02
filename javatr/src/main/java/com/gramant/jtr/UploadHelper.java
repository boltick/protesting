package com.gramant.jtr;

import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ab83625
 * Date: 10.05.2010
 * To change this template use File | Settings | File Templates.
 */
public class UploadHelper {

    public static final int SFTP_PROTOCOL = 1;
    public static final int FTP_PROTOCOL = 2;
    public static final int FILE_PROTOCOL = 3;

    private int protocol;
    private String srvHost;
    private String userName;
    private String password;
    private String destination;


    public UploadHelper(int protocol, String srvHost, String userName, String password) {
        this.protocol = protocol;
        this.srvHost = srvHost;
        this.userName = userName;
        this.password = password;
    }

    public UploadHelper(String protocol, String srvHost, String userName, String password) {
       this(Integer.parseInt(protocol), srvHost, userName, password);
    }


    public boolean upload(String srcFileName, String distFileName) {
        try {
            File srcFile = new File(srcFileName);


            switch(protocol) {
                case SFTP_PROTOCOL: {
                    sftpUpload(srcFile, distFileName);
                    break;
                }
                case FTP_PROTOCOL: {
                    ftpUpload(srcFileName, distFileName);
                    break;
                }
                case FILE_PROTOCOL: {
                    File distFile = new File(distFileName + srcFile.getName());
                    if (distFile.exists()) {
                        distFile.delete();
                    }

                    fileUpload(srcFile, distFile);
                    break;
                }
                default: {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void fileUpload(File srcFileName, File distFileName) throws IOException {
        copyResultFile(srcFileName, distFileName);
    }


    private void sftpUpload(File srcFileName, String distLocation) throws IOException {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftpChannel = null;
        try {
            session = jsch.getSession(userName, srvHost, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
            sftpChannel.put(srcFileName.getAbsolutePath(), distLocation + srcFileName.getName());
        } catch (JSchException e) {
            e.printStackTrace();
            throw new IOException(e.getLocalizedMessage());
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getLocalizedMessage());
        } finally {
            if (session!=null) {
                session.disconnect();
            }
            if (sftpChannel!=null) {
                sftpChannel.exit();
            }
        }
    }

    private void ftpUpload(String srcFileName, String distLocation) throws IOException {
        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        client.connect(srvHost);
        client.login(userName, password);
        client.changeWorkingDirectory(distLocation);
        String filename = srcFileName;
        fis = new FileInputStream(filename);
        client.storeFile(filename, fis);
        client.logout();
        fis.close();

    }

    private void copyResultFile(File srFile, File dtFile) throws IOException {
        try{
            InputStream in = new FileInputStream(srFile);
            OutputStream out = new FileOutputStream(dtFile);

            byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }


    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getSrvHost() {
        return srvHost;
    }

    public void setSrvHost(String srvHost) {
        this.srvHost = srvHost;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
