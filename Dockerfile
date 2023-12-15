FROM logstash:8.11.1
WORKDIR /usr/share/logstash
COPY logstash-filter-mask_sensitive_data_filter-*.gem /usr/share/logstash/logstash-filter-mask_sensitive_data_filter.gem
RUN bin/logstash-plugin install --no-verify --local /usr/share/logstash/logstash-filter-mask_sensitive_data_filter.gem