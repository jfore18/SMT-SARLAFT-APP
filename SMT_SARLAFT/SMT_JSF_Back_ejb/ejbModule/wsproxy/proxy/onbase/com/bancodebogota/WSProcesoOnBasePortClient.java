package wsproxy.proxy.onbase.com.bancodebogota;

import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service.DocumentationItemManagement;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service.DocumentationItemManagementSvc;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service.SetDocumentFault_Exception;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service.SetDocumentResponse;
import wsproxy.proxy.onbase.com.bancodebogota.resources.classification.service.SetDocumentRequest;

public class WSProcesoOnBasePortClient {

   DocumentationItemManagementSvc wSProcesoDocumentos_Service = new DocumentationItemManagementSvc();
   DocumentationItemManagement wSProcesoDocumentos =  wSProcesoDocumentos_Service.getDocumentationItemManagementPort();
    
   public SetDocumentResponse getEntregarDocumento(SetDocumentRequest datosDocumento) throws SetDocumentFault_Exception {
         return this.wSProcesoDocumentos.setDocument(datosDocumento);
   }

}
