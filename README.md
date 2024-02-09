# Sample AEM project template

This is a project template for AEM-based applications. It is intended as a best-practice set of examples as well as a potential starting point to develop your own functionality.

## Modules

The main parts of the template are:

* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* ui.apps: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, and templates
* ui.content: contains sample content using the components from the ui.apps
* ui.config: contains runmode specific OSGi configs for the project
* all: a single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

## Testing

There are three levels of testing contained in the project:

### Unit tests

This show-cases classic unit testing of the code contained in the bundle. To
test, execute:

    mvn clean test

# How to download (CLI)

1. Execute the following cURL command 
```
curl -O --header "Authorization: Bearer BEARER_TOKEN" https://maven.pkg.github.com/pat-lego/support-pdfviewer/com/adobe/aem/support/pdfviewer.all/1.0.0-SNAPSHOT/pdfviewer.all-1.0.0-SNAPSHOT.zip
```
2. Follow these steps to embed the dependency in the project
   https://medium.com/@jakubtutko/maven-repository-inside-your-project-4c55b4d73be8

# How to use in your Maven project

1. Create a settings.xml file in your project and add the following to it
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <profiles>
    <profile>
      <repositories>
        <repository>
          <id>github</id>
          <name>Github</name>
          <url>https://maven.pkg.github.com/pat-lego/support-pdfviewer</url>
        </repository>
      </repositories>
    </profile>
  </profiles>
  <servers>
    <server>
      <id>github</id>
      <username>pat-lego</username>
      <password>BEARER_TOKEN</password>
    </server>
  </servers>
</settings>
```
2. Add the following dependency to your project 
```
<dependency>
  <groupId>com.adobe.aem.support</groupId>
  <artifactId>pdfviewer.all</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <type>zip</type>
</dependency>
```
3. Add the following repositories tag in your pom file
```
<repositories>
    <repository>
      <id>github</id>
      <name>Github</name>
      <releases>
        <enabled>false</enabled>
        <updatePolicy>always</updatePolicy>
        <checksumPolicy>warn</checksumPolicy>
      </releases>
      <snapshots>
        <enabled>true</enabled>
        <updatePolicy>never</updatePolicy>
        <checksumPolicy>fail</checksumPolicy>
      </snapshots>
      <url>https://maven.pkg.github.com/pat-lego/support-pdfviewer</url>
      <layout>default</layout>
    </repository>
  </repositories>
```
4. Run a mvn build