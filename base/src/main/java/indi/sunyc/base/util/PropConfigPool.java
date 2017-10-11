package indi.sunyc.base.util;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ChamIt-001 on 2017/9/11.
 */
public class PropConfigPool {

    private static final Logger logger = LoggerFactory.getLogger(PropConfigPool.class);
    private static List<PropConfiguration> configurationList;
    private static Map<String,String> propList;
    private static List<ScanPathItem> scanPathList;
    private static Boolean isInitialized = false;

    public static void putScanPath(String path,String suffix){
        if(scanPathList == null){
            scanPathList = new ArrayList<ScanPathItem>();
        }
        ScanPathItem scanPathItem = new ScanPathItem(path,suffix);
        if(!scanPathList.contains(scanPathItem)) {
            scanPathList.add(scanPathItem);
        }
    }

    public static void init() {
        configurationList = new ArrayList<PropConfiguration>();
        propList = new HashMap<String,String>();
        try {
            if(scanPathList == null){
                putScanPath(Thread.currentThread().getContextClassLoader().getResource("").getPath(),"properties");
            }
            for(ScanPathItem scanPathItem:scanPathList) {
                File root = new File(scanPathItem.getPath());
                if (root != null) {
                    for (File file : root.listFiles()) {
                        if (file.isFile() && file.getName().substring(file.getName().lastIndexOf(".") + 1).equals(scanPathItem.getSuffix())) {
                            load(file.getName());
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("PropConfigPool init exception:{}", ExceptionUtils.getStackTrace(e));
        }finally {
            isInitialized = true;
        }
    }

    private PropConfigPool(){}

    public static void load(String filename) {
        if(!isInitialized){
            init();
        }
        try {
            for (PropConfiguration configuration : configurationList) {
                if (filename.equals(configuration.getFileName())) {
                    configuration.load();
                    return;
                }
            }
            PropConfiguration newConfig = new PropConfiguration(filename,propList);
            configurationList.add(newConfig);
        } catch (ConfigurationException ce) {
            logger.error("PropConfigPool load exception:{}", ExceptionUtils.getStackTrace(ce));
        }
    }

    public static String getString(String key) {
        return propList.get(key);
    }

    public static String getString(String key,String defaultProp) { return getString(key) == null ? defaultProp : propList.get(key); }

    public static Long getLong(String key) { return propList.get(key) == null ? null : new Long(propList.get(key)); }

    public static Long getLong(String key,Long defaultProp) { return getLong(key) == null ? defaultProp : getLong(key); }
}

class PropConfiguration extends ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(PropConfiguration.class);
    private Map<String,String> propList;
    private String fileName;
    private PropertiesConfiguration properties;
    private ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> reloadBuilder;

    public String getFileName() {
        return fileName;
    }

    private PropConfiguration(){
        super(PropertiesConfiguration.class);
    }

    public PropConfiguration(String filename,Map<String,String> propList) throws ConfigurationException {
        super(PropertiesConfiguration.class);
        this.propList = propList;
        this.fileName = filename;
        Parameters params = new Parameters();
        File propertiesFile = new File(this.fileName);
        this.configure(params.fileBased().setEncoding("UTF-8").setFile(propertiesFile));
        PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(this.getReloadingController(), null, 1, TimeUnit.HOURS);
        trigger.start();
        this.load();
    }

    public void load() throws ConfigurationException {
        try {
            properties = (PropertiesConfiguration) reloadBuilder.getConfiguration();
            setPropList();
        } catch (ConfigurationException e) {
            logger.error("ConfigurationException in PropConfiguration.load()", e);
        }
    }

    private void setPropList(){
        try {
            Iterator<String> iterator = properties.getKeys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = properties.getString(key);
                propList.put(key, properties.getString(key));
            }
        }catch (Exception e) {
            //
        }
    }
}

class ScanPathItem {
    private String path;
    private String suffix;

    public String getPath() {
        return path;
    }

    public String getSuffix() {
        return suffix;
    }

    public ScanPathItem(String path, String suffix){
        this.path = path;
        this.suffix = suffix;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof ScanPathItem)){
            return false;
        }
        ScanPathItem o = (ScanPathItem)obj;
        if(this.getPath().equals(o.getPath()) && this.getSuffix().equals(o.getSuffix())){
            return true;
        } else {
            return false;
        }
    }
}
