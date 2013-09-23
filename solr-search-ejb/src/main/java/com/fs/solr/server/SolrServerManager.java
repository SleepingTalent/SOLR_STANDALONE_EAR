package com.fs.solr.server;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class SolrServerManager {

    private static Logger log = Logger.getLogger(SolrServerManager.class);

    public static final String SOLR_SERVER_URL = "http://localhost:8080/solr";

    public static final String EMPLOYEES_CORE = "employees";

    private static String getSolrUrl() {
        String url = System.getProperty("solr.server.url");

        if(url != null && !url.isEmpty()) {
            log.debug("Setting Solr Server Url to : " + url);
            return url;
        }else {
            log.debug("solr.server.url not found using default : " + SOLR_SERVER_URL);
            return SOLR_SERVER_URL;
        }
    }

    private static SolrServer getSolrServer(String solrCore) {
       return new HttpSolrServer(getSolrUrl() + "/" + solrCore);
    }

    public static SolrServer getSolrServerForEmployees() {
        return getSolrServer(EMPLOYEES_CORE);
    }
}
