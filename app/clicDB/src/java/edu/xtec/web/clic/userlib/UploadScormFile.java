/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic.userlib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author fbusquet
 */
public class UploadScormFile extends GetUserInfo {

  String status;
  String err;
  UserProject prj = null;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();
    try {
      loadUserData(request);
      if (email != null && userSpace != null) {
        if (!ServletFileUpload.isMultipartContent(request)) {
          status = "error";
          err = "Transacció incorrecta";
        } else {
          // Create a factory for disk-based file items
          DiskFileItemFactory factory = new DiskFileItemFactory();
          // Configure a repository (to ensure a secure temp location is used)
          ServletContext servletContext = request.getSession().getServletContext();
          File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
          // Set factory constraints
          // factory.setSizeThreshold(yourMaxMemorySize);
          factory.setRepository(repository);
          // Create a new file upload handler
          ServletFileUpload upload = new ServletFileUpload(factory);
          // Set overall request size constraint
          // upload.setSizeMax(yourMaxRequestSize);

          // Parse the request
          FileItem theFile = null;
          String projectName = null;
          Iterator it = upload.parseRequest(request).iterator();
          while (it.hasNext()) {
            FileItem item = (FileItem) it.next();
            if (item.isFormField()) {
              if (item.getFieldName().equals("project")) {
                projectName = item.getString();
              }
            } else if (theFile == null) {
              theFile = item;
            }
          }
          if (theFile != null) {
            processUploadedFile(theFile, projectName);
            setSessionAttributes(request.getSession(false));
          }
        }
      }
      writer.write(getJsonResponse().toString());
      if ("ok".equals(status) && prj != null) {
        logMsg("INFO", "Project uploaded: \"" + prj.name + "\"");
      } else {
        logMsg("ERROR", "Error uploading project " + (prj != null ? prj.name : "unknown") + ": " + err);
      }
    } catch (Exception ex) {
      writer.print("{\"status\":\"error\",\"error\":\"" + JSONStringer.getString(ex.getMessage()) + "\"}");
      logMsg("ERROR", "Error uploading project " + (prj != null ? prj.name : "unknown") + ": " + ex.getMessage());
    } finally {
      writer.flush();
    }
  }

  protected void processUploadedFile(FileItem item, String projectName) throws Exception {
    String fieldName = item.getFieldName();
    String fileName = item.getName();
    String contentType = item.getContentType();
    //boolean isInMemory = item.isInMemory();
    long sizeInBytes = item.getSize();

    if (fieldName == null || !fieldName.equals("scormFile")
            || fileName == null || !fileName.endsWith(".scorm.zip")
            || !"application/zip".equals(contentType)) {
      status = "error";
      err = "Tipus de fitxer incorrecte!";
    } else if (sizeInBytes > (quota - userSpace.currentSize)) {
      status = "error";
      err = "Heu excedit l'espai disponible!";
    } else {

      // Get a valid project name
      if (projectName == null || "".equals(projectName)) {
        projectName = fileName.substring(0, fileName.indexOf('.'));
      }
      
      projectName = UserProject.getValidName(projectName);

      // Reset project if exists
      userSpace.removeProject(projectName);
      prj = new UserProject(projectName, userSpace);

      String prjBase = prj.prjRoot.getCanonicalPath() + File.separator;

      // Treat input as a ZIP stream
      ZipInputStream zis = new ZipInputStream(new BufferedInputStream(item.getInputStream()));
      ZipEntry entry;
      boolean interrupted = false;
      int fileCount = 0;
      while ((entry = zis.getNextEntry()) != null) {
        // Process files
        File entryFile = new File(prj.prjRoot, entry.getName());
        if (!entryFile.getCanonicalPath().startsWith(prjBase)) {
          // File is out of project's directory
          prj.clean();
          interrupted = true;
          break;
        }
        if (entry.isDirectory()) {
          entryFile.mkdirs();
        } else {
          entryFile.getParentFile().mkdirs();
          IOUtils.copy(zis, new FileOutputStream(entryFile));
          fileCount++;
        }
      }
      zis.close();

      // Add project to collection
      userSpace.addProject(prj);

      if (interrupted) {
        status = "error";
        err = "Nom de fitxer incorrecte dins del fitxer ZIP";
      } else if (fileCount == 0) {
        status = "error";
        err = "El fitxer no tenia cap contingut vàlid";
      } else {
        status = "ok";
      }
    }
  }

  public JSONObject getJsonResponse() throws Exception {
    JSONObject json = new JSONObject();
    if (email == null || userSpace == null) {
      json.put("status", "error");
      json.put("error", "Usuari incorrecte");
    } else {
      json.put("status", status);
      if (status.equals("ok")) {
        json.put("project", prj.getJSON());
      } else if (err != null) {
        json.put("error", err);
      }
    }
    return json;
  }

}
