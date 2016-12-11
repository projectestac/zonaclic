/*
 * Probablement no funcionarà amb IAS 10.1.2, ja que està basat en Java Servlet 2.3 (veure https://docs.oracle.com/cd/B14099_19/web.1012/b14017.pdf) i
 * i la directiva "MultipartConfig" apareix amb la versió 3 (veure http://docs.oracle.com/middleware/1221/wls/WBAPP/configureservlet.htm#WBAPP707)
 */
package edu.xtec.web.clic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.json.JSONObject;

/**
 *
 * @author Francesc Busquets <francesc@gmail.com>
 */
public class UsrUploadBean extends UsrLibBean {

  String fileName;
  String projectName;
  String status = "processing";
  String err;

  //@Override
  protected void getRequestParams(HttpServletRequest request) throws Exception {
    super.getRequestParams(request);
    if (email != null) {
      final Part filePart = request.getPart("file");
      if (filePart != null) {
        fileName = getFileName(filePart);
        if (!fileName.endsWith(".scorm.zip")) {
          status = "error";
          err = "Invalid file type!";
        } else {
          projectName = fileName.substring(0, fileName.indexOf('.'));
          File dest = new File(userSpace.root, "__TMP" + projectName +".zip");
          OutputStream out = null;
          InputStream filecontent = null;
          try {
            out = new FileOutputStream(dest);
            filecontent = filePart.getInputStream();
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
              out.write(bytes, 0, read);
            }
            status = "ok";
          } catch (Exception ex) {
            status = "error";
            err = ex.getMessage();
          } finally {
            if (out != null) {
              out.close();
            }
            if (filecontent != null) {
              filecontent.close();
            }
          }
        }
      }
    }
  }

  private String getFileName(final Part part) {
    String header = part.getHeader("content-disposition");
    if (header != null) {
      String[] chunks = header.split(";");
      for (int i = 0; i < chunks.length; i++) {
        if (chunks[i].trim().startsWith("filename")) {
          return chunks[i].substring(chunks[i].indexOf('=') + 1).trim().replace("\"", "");
        }
      }
    }
    return null;
  }

  public String getJsonResponse() {
    String result;
    try {
      JSONObject json = new JSONObject();
      if (email == null) {
        json.put("status", "error");
        json.put("error", "invalid user");
      } else {
        json.put("status", status);
        if (err != null) {
          json.put("error", err);
        }
      }
      result = json.toString(1);
    } catch (Exception ex) {
      result = "{\"status\":\"error\",\"error\":\"invalid data\"}";
    }
    return result;
  }

}
