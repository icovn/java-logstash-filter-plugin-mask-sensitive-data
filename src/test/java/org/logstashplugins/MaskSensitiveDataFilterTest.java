package org.logstashplugins;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Event;
import co.elastic.logstash.api.FilterMatchListener;
import org.junit.Assert;
import org.junit.Test;
import org.logstash.plugins.ConfigurationImpl;
import org.logstash.plugins.ContextImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MaskSensitiveDataFilterTest {

    @Test
    public void testJavaExampleFilter() {
        String patternValue = "phone:,email:";
        String sourceField = "message";
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("pattern", patternValue);
        configMap.put("source", sourceField);
        Configuration config = new ConfigurationImpl(configMap);
        Context context = new ContextImpl(null, null);
        MaskSensitiveDataFilter filter = new MaskSensitiveDataFilter("test-id", config, context);

        Event e = new org.logstash.Event();
        TestMatchListener matchListener = new TestMatchListener();
        e.setField("pattern", patternValue);
        e.setField(sourceField, "Customer phone:0123456789, email:abc@gmail.com, id:9, has phone:0123456789");
        Collection<Event> results = filter.filter(Collections.singletonList(e), matchListener);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Customer phone:***, email:***, id:9, has phone:***", e.getField(sourceField));
        Assert.assertEquals(1, matchListener.getMatchCount());
    }
}

class TestMatchListener implements FilterMatchListener {

    private AtomicInteger matchCount = new AtomicInteger(0);

    @Override
    public void filterMatched(Event event) {
        matchCount.incrementAndGet();
    }

    public int getMatchCount() {
        return matchCount.get();
    }
}