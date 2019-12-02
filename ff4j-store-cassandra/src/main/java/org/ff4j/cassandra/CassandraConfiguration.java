package org.ff4j.cassandra;

/*-
 * #%L
 * ff4j-store-cassandra
 * %%
 * Copyright (C) 2013 - 2019 FF4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.dropKeyspace;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cassandraguide.repository.ReservationRepositoryWithQueryBuilder;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Import Configuration from Configuration File
 */
public class CassandraConfiguration {
    
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(CassandraConfiguration.class);
    
    protected String host;
    
    protected int port;
    
    protected String localDataCenterName = "datacenter1";
    
    // KeySpace Name
    public String keyspaceName = "reservation";
    
    // Do you want to drop schema and generate tables at startup
    @Value("${cassandra.dropSchema:true}")
    public boolean dropSchema;

    /**
     * Default configuration.
     */
    public CassandraConfiguration() {}
            
    /**
     * Initialization of Configuration.
     *
     * @param cassandraHost
     * @param cassandraPort
     * @param localDataCenterName
     * @param keyspaceName
     * @param dropSchema
     */
    public CassandraConfiguration(
            String cassandraHost, int cassandraPort, String localDataCenterName, 
            String keyspaceName,  boolean dropSchema) {
        super();
        this.cassandraHost       = cassandraHost;
        this.cassandraPort       = cassandraPort;
        this.keyspaceName        = keyspaceName;
        this.dropSchema          = dropSchema;
        this.localDataCenterName = localDataCenterName;
    }
    
    /**
     * Returns the keyspace to connect to. The keyspace specified here must exist.
     *
     * @return The {@linkplain CqlIdentifier keyspace} bean.
     */
    @Bean
    public CqlIdentifier keyspace() {
      return CqlIdentifier.fromCql(keyspaceName);
    }
    
    @Bean
    public CqlSession cqlSession() {
        logger.info("Creating (eventually dropping) keyspace and table to init");
        try(CqlSession tmpSession = CqlSession.builder()
                               .addContactPoint(new InetSocketAddress(getCassandraHost(), getCassandraPort()))
                               .withLocalDatacenter(getLocalDataCenterName())
                               .build()) {
            if (isDropSchema()) {
                tmpSession.execute(dropKeyspace(keyspace()).ifExists().build());
                logger.debug("+ Keyspace '{}' has been dropped (if existed)", keyspace());
            }
            tmpSession.execute(createKeyspace(keyspace()).ifNotExists()
                    .withSimpleStrategy(1)    // 1 is the replication factor
                    .withDurableWrites(true)
                    .build());
            logger.debug("+ Keyspace '{}' has been created (if needed)", keyspace());
        }
        logger.info("Successfully initialized.");
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(getCassandraHost(), getCassandraPort()))
                .withKeyspace(keyspace())
                .withLocalDatacenter(getLocalDataCenterName())
                .build();
    }

    /**
     * Getter accessor for attribute 'cassandraHost'.
     *
     * @return
     *       current value of 'cassandraHost'
     */
    public String getCassandraHost() {
        return cassandraHost;
    }

    /**
     * Setter accessor for attribute 'cassandraHost'.
     * @param cassandraHost
     * 		new value for 'cassandraHost '
     */
    public void setCassandraHost(String cassandraHost) {
        this.cassandraHost = cassandraHost;
    }

    /**
     * Getter accessor for attribute 'cassandraPort'.
     *
     * @return
     *       current value of 'cassandraPort'
     */
    public int getCassandraPort() {
        return cassandraPort;
    }

    /**
     * Setter accessor for attribute 'cassandraPort'.
     * @param cassandraPort
     * 		new value for 'cassandraPort '
     */
    public void setCassandraPort(int cassandraPort) {
        this.cassandraPort = cassandraPort;
    }

    /**
     * Getter accessor for attribute 'localDataCenterName'.
     *
     * @return
     *       current value of 'localDataCenterName'
     */
    public String getLocalDataCenterName() {
        return localDataCenterName;
    }

    /**
     * Setter accessor for attribute 'localDataCenterName'.
     * @param localDataCenterName
     * 		new value for 'localDataCenterName '
     */
    public void setLocalDataCenterName(String localDataCenterName) {
        this.localDataCenterName = localDataCenterName;
    }

    /**
     * Getter accessor for attribute 'keyspaceName'.
     *
     * @return
     *       current value of 'keyspaceName'
     */
    public String getKeyspaceName() {
        return keyspaceName;
    }

    /**
     * Setter accessor for attribute 'keyspaceName'.
     * @param keyspaceName
     * 		new value for 'keyspaceName '
     */
    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    /**
     * Getter accessor for attribute 'dropSchema'.
     *
     * @return
     *       current value of 'dropSchema'
     */
    public boolean isDropSchema() {
        return dropSchema;
    }

    /**
     * Setter accessor for attribute 'dropSchema'.
     * @param dropSchema
     * 		new value for 'dropSchema '
     */
    public void setDropSchema(boolean dropSchema) {
        this.dropSchema = dropSchema;
    }
    
    

}
