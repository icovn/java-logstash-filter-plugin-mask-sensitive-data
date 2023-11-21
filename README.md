# Requirement

- [Java](https://www.elastic.co/guide/en/logstash/current/getting-started-with-logstash.html)
- [Example filter plugin](https://github.com/logstash-plugins/logstash-filter-java_filter_example)
- [Logstash binary](https://www.elastic.co/downloads/logstash)

# How to

### Step 1: Get Logstash source code
```shell
git clone --branch <branch_name> --single-branch https://github.com/elastic/logstash.git <target_folder>
```
- The branch_name must match with the Logstash binary downloaded above, and should correspond to the version of 
Logstash containing the preferred revision of the Java plugin API
- Specify the target_folder for your local copy of the Logstash codebase. If you do not specify target_folder, 
it defaults to a new folder called logstash under your current folder.
- Example:
    ```shell
    git clone --branch 8.10 --single-branch https://github.com/elastic/logstash.git /Volumes/MacData/personal/logstash
    ```

### Step 2: Generate logstash-core
```shell
cd <target_folder>
./gradlew assemble
```
- Example
    ```shell
    cd /Volumes/MacData/personal/logstash
    ./gradlew assemble
    ```

### Step 3: Update the Java plugin config
Create a new file named gradle.properties in the root folder of your plugin project. That file should have a single 
line:
```
LOGSTASH_CORE_PATH=<target_folder>/logstash-core
```
- Example
    ```
    LOGSTASH_CORE_PATH=/Volumes/MacData/personal/logstash/logstash-core
    ```

### Step 4: Update the Java plugin code

### Step 5: Build the plugin
Update the build.grade file with the information you want
```
// ===========================================================================
// plugin info
// ===========================================================================
group                      'org.logstashplugins' // must match the package of the main plugin class
version                    "${file("VERSION").text.trim()}" // read from required VERSION file
description                = "Example Java filter implementation"
pluginInfo.licenses        = ['Apache-2.0'] // list of SPDX license IDs
pluginInfo.longDescription = "This gem is a Logstash plugin required to be installed on top of the Logstash core pipeline using \$LS_HOME/bin/logstash-plugin install gemname. This gem is not a stand-alone program"
pluginInfo.authors         = ['Elasticsearch']
pluginInfo.email           = ['info@elastic.co']
pluginInfo.homepage        = "http://www.elastic.co/guide/en/logstash/current/index.html"
pluginInfo.pluginType      = "filter"
pluginInfo.pluginClass     = "JavaFilterExample"
pluginInfo.pluginName      = "java_filter_example"
// ===========================================================================
```

Build gem
```shell
./gradlew gem
```
- That task will produce a gem file in the root directory of your plugin’s codebase with the name 
logstash-{plugintype}-<pluginName>-<version>.gem

### Step 6: Install the Java plugin in Logstash
```shell
cd <logtash_binary>
bin/logstash-plugin install --no-verify --local /path/to/javaPlugin.gem
```
- Example
    ```shell
    cd /Volumes/MacData/application/logstash-8.10.2
    bin/logstash-plugin install --no-verify --local /Volumes/MacData/personal/logstash-filter-java_filter_example/logstash-filter-java_filter_example-1.0.3.gem
    ```

### Step 7: Run Logstash with the Java filter plugin
Create a test configuration file name test_java_filter.conf with below content
```
input {
  generator { 
    message => "Customer phone:0123456789, email=abc@gmail.com, id:9, has phone:0123456789" 
    count => 1 
  }
}
filter {
  mask_sensitive_data_filter {
    source => "message"
    pattern => "phone:,email="
  }
}
output {
  stdout { codec => rubydebug }
}
```

Run test
```shell
cd <logtash_binary>
bin/logstash -f /path/to/java_filter.conf
```
- Example
    ```shell
    cd /Volumes/MacData/application/logstash-8.10.2
    bin/logstash -f test_java_filter.conf
    ```

# Reference
- [How to write a Java filter plugin](https://www.elastic.co/guide/en/logstash/current/java-filter-plugin.html)
- [Get Started with Logstash Plugin Development](https://www.youtube.com/watch?v=_gd5-PX5KeE)
- When you get the error below
```
* Where:
Script '/Volumes/MacData/personal/logstash/rubyUtils.gradle' line: 26

* What went wrong:
A problem occurred evaluating script.
> Could not get unknown property 'snakeYamlVersion' for object of type org.gradle.api.internal.artifacts.dsl.dependencies.DefaultDependencyHandler.
```
You can update the rubyUtils.gradle file at line 16 from
```
classpath "org.yaml:snakeyaml:${snakeYamlVersion}"
```
to
```
classpath "org.yaml:snakeyaml:2.0"
```
