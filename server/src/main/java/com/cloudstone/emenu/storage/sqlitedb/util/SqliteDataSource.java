/**
 * @(#)SqliteDataSource.java, Aug 2, 2013. 
 *
 */

package com.cloudstone.emenu.storage.sqlitedb.util;

import java.io.File;
import java.io.IOException;

import com.cloudstone.emenu.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;

/**
 * @author xuhongfeng
 */
@Component
public class SqliteDataSource  {
    private static final Logger LOG = LoggerFactory.getLogger(SqliteDataSource.class);

    private File dbFile = null;

    public File getDbFile() {
        if (dbFile == null) {
            String dbFilePath = AppConfig.getDBFilePath();
            LOG.info("DB file location: " + dbFilePath);
            dbFile = new File(dbFilePath);
        }
        if (!dbFile.exists()) {
            synchronized (SqliteDataSource.class) {
                if (!dbFile.exists()) {
                    try {
                        LOG.info("Creating new DB file at: " + dbFile.getAbsolutePath());
                        dbFile.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return dbFile;
    }

    public void setDbFile(File dbFile) {
        this.dbFile = dbFile;
    }

    public synchronized DbTransaction openTrans() {
        waitForTransaction();
        inTransaction = true;
        return new DbTransaction(new SQLiteConnection(getDbFile()));
    }

    public synchronized SQLiteConnection open() throws SQLiteException {
        waitForTransaction();
        SQLiteConnection conn = new SQLiteConnection(getDbFile());
        conn.open();
        return conn;
    }

    private boolean inTransaction = false;

    public synchronized void waitForTransaction() {
        if (inTransaction) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                LOG.error("", e);
            }
        }
    }

    public synchronized void notifyTransactionDone() {
        inTransaction = false;
        this.notifyAll();
    }
}
