/*
 * Probablement no funcionarà amb IAS 10.1.2, ja que està basat en Java Servlet 2.3 (veure https://docs.oracle.com/cd/B14099_19/web.1012/b14017.pdf) i
 * i la directiva "MultipartConfig" apareix amb la versió 3 (veure http://docs.oracle.com/middleware/1221/wls/WBAPP/configureservlet.htm#WBAPP707)
 */
package edu.xtec.web.clic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

/**
 *
 * @author Francesc Busquets <francesc@gmail.com>
 */
public class UsrUploadBean extends UsrLibBean {

  String status = "processing";
  String err;
  UserProject prj = null;

  //@Override
  protected void getRequestParams(HttpServletRequest request) throws Exception {
    super.getRequestParams(request);
    if (email != null && userSpace != null) {
      status = "error";
      err = "bad request";
      if (ServletFileUpload.isMultipartContent(request)) {

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = request.getSession().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        // Set factory constraints
        //factory.setSizeThreshold(yourMaxMemorySize);
        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // Set overall request size constraint
        //upload.setSizeMax(yourMaxRequestSize);

        // Parse the request
        FileItem theFile = null;
        String projectName = null;
        Iterator it = upload.parseRequest(request).iterator();
        while (it.hasNext()) {
          FileItem item = (FileItem) it.next();
          if(item.isFormField()){
            if(item.getFieldName().equals("project"))
              projectName = item.getString();            
          } else if(theFile == null)
            theFile = item;
        }
        if(theFile!=null) {
          processUploadedFile(theFile, projectName);
          setSessionAttributes(request.getSession(false));
        }
      }
    } else {
      status = "error";
      err = "bad user";
    }
  }

  protected void processUploadedFile(FileItem item, String projectName) throws Exception {
    String fieldName = item.getFieldName();
    String fileName = item.getName();
    String contentType = item.getContentType();
    //boolean isInMemory = item.isInMemory();
    long sizeInBytes = item.getSize();
    if ("scormFile".equals(fieldName)
            && fileName != null
            && fileName.endsWith(".scorm.zip")
            && "application/zip".equals(contentType)) {
      if (sizeInBytes > (quota - userSpace.currentSize)) {
        err = "disk quota exceeded";
        return;
      }
      
      if(projectName == null || "".equals(projectName))
        projectName = fileName.substring(0, fileName.indexOf('.'));
      
      try {
        userSpace.removeProject(projectName);
        prj = new UserProject(projectName, userSpace);
        
        String prjBase = prj.prjRoot.getCanonicalPath() + File.separator;

        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(item.getInputStream()));
        ZipEntry entry;
        boolean interrupted = false;
        while ((entry = zis.getNextEntry()) != null) {
          File entryFile = new File(prj.prjRoot, entry.getName());
          if (!entryFile.getCanonicalPath().startsWith(prjBase)) {
            prj.clean();
            err = "Invalid file name in ZIP file";
            interrupted = true;
            break;
          }
          if (entry.isDirectory()) {
            entryFile.mkdirs();
          } else {
            entryFile.getParentFile().mkdirs();
            IOUtils.copy(zis, new FileOutputStream(entryFile));
          }
        }
        zis.close();
        userSpace.addProject(prj);
        if (!interrupted) {
          status = "ok";
        }
      } catch (Exception ex) {
        err = ex.getMessage();
      }
    } else {
      err = "bad file type";
    }
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
        if(status.equals("ok")) {
          json.put("project", prj.getJSON());          
        } else if(err!=null){
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
