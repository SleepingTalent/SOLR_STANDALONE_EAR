package com.fs.solr.index;

import com.fs.solr.exception.ReIndexException;
import com.fs.solr.server.SolrServerManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;

import java.io.IOException;

public class ReIndexHelper {

    public static final Logger log = Logger.getLogger(ReIndexHelper.class);

    public static final String DATAIMPORT_REQUEST = "/dataimport";
    public static final String DELTA_IMPORT = "delta-import";

    public static final String STATUS = "status";
    public static final String IDLE_STATUS = "idle";

    public void performReIndexOnEmployees() throws ReIndexException {
        reIndex(SolrServerManager.getSolrServerForEmployees(), "Employees");
    }

    private void reIndex(SolrServer solrServer, String indexType) throws ReIndexException {
        try {

            if (getReIndexStatus(solrServer, indexType).equals(IDLE_STATUS)) {
                startReIndex(solrServer, indexType);
            } else {
                log.info("Delta-Import already running on : " + indexType + " skipping Index execution!");
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ReIndexException(e.getMessage(), e);
        } catch (SolrServerException sse) {
            log.error(sse.getMessage(), sse);
            throw new ReIndexException(sse.getMessage(), sse);
        }
    }

    private void startReIndex(SolrServer solrServer, String indexType) throws ReIndexException {

        ModifiableSolrParams dataImportParams = new ModifiableSolrParams();
        dataImportParams.set("qt", DATAIMPORT_REQUEST);
        dataImportParams.set("command", DELTA_IMPORT);
        dataImportParams.set("commit", "true");

        try {
            log.info("Running Delta-Import on : " + indexType);
            solrServer.query(dataImportParams);
        } catch (SolrServerException e) {
            log.error(e.getMessage(), e);
            throw new ReIndexException(e.getMessage(), e);
        }
    }

    private String getReIndexStatus(SolrServer solrServer, String indexType) throws IOException, SolrServerException {
        SolrRequest dataImportStatusRequest = new DirectXmlRequest(DATAIMPORT_REQUEST, null);
        NamedList<Object> responseList = solrServer.request(dataImportStatusRequest);

        String importStatus = (String) responseList.get(STATUS);

        log.debug("Retrieved Import Status for (" + indexType + ") : " + importStatus);

        return importStatus;
    }
}
