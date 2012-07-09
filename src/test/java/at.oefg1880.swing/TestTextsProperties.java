package at.oefg1880.swing;

import junit.framework.TestCase;
import java.util.Properties;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: AT003053
 * Date: 09.07.12
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */
public class TestTextsProperties extends TestCase {
    Properties default_properties;
    Properties en_properties;

    public TestTextsProperties(String s) {
        super(s);
    }

    @Override
    protected void setUp() throws Exception {
        default_properties = new Properties();
        default_properties.load(getClass().getClassLoader().getResourceAsStream("resources/texts.properties"));
        en_properties = new Properties();
        en_properties.load(getClass().getClassLoader().getResourceAsStream("resources/texts_en.properties"));
    }

    public void testConsistent() {
        // each property that exists in default_properties must exist in en_properties
        String sError = "";

        Set<Object> keys = default_properties.keySet();
        for (Object key : keys) {
            String sKey = key.toString();
//            assertTrue ("Property does not exist in text_en: " + sKey, en_properties.getProperty(sKey) != null);
            if (en_properties.getProperty(sKey) == null) {
                sError += "Property Missing: " + sKey + "\r\n";
            }
        }
        assertTrue("Properties Missing: \r\n" + sError, sError.length() == 0);
    }
}
