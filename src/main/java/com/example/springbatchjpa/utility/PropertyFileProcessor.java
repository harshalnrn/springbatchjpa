package com.example.springbatchjpa.utility;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Component
public class PropertyFileProcessor {

    public String getPropertyValue(String key) {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        Environment environment;
        try {

            fileInputStream = new FileInputStream("D:\\application_config.properties");
            properties.load(fileInputStream);


        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {

            }
        }

        return properties.getProperty(key);
    }
}
