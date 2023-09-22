package org.logstashplugins;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Event;
import co.elastic.logstash.api.Filter;
import co.elastic.logstash.api.FilterMatchListener;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;
import java.util.Arrays;

import java.util.Collection;

// class name must match plugin name
@LogstashPlugin(name = "mask_sensitive_data_filter")
public class MaskSensitiveDataFilter implements Filter {

    public static final PluginConfigSpec<String> PATTERN_CONFIG =
            PluginConfigSpec.stringSetting("pattern", "");
    public static final PluginConfigSpec<String> SOURCE_CONFIG =
            PluginConfigSpec.stringSetting("source", "message");

    private String id;
    private String pattern;
    private String sourceField;

    public MaskSensitiveDataFilter(String id, Configuration config, Context context) {
        // constructors should validate configuration options
        this.id = id;
        this.pattern = config.get(PATTERN_CONFIG);
        this.sourceField = config.get(SOURCE_CONFIG);
    }

    @Override
    public Collection<Event> filter(Collection<Event> events, FilterMatchListener matchListener) {
        for (Event event : events) {
            Object fieldData = event.getField(sourceField);
            if (fieldData instanceof String) {
                event.setField(sourceField, SecurityUtil.maskSensitive((String)fieldData, pattern));
                matchListener.filterMatched(event);
            }
        }
        return events;
    }

    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {
        // should return a list of all configuration options for this plugin
        return Arrays.asList(PATTERN_CONFIG, SOURCE_CONFIG);
    }

    @Override
    public String getId() {
        return this.id;
    }
}
