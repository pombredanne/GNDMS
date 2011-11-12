# Generated by Buildr 1.4.6, change to your liking
#
require 'buildr/java'
include Java
include Commands

require 'buildr/adis_modules.rb'
include ADIS_MODULES

setupModules '../vold', 'vold'

# Version number for this release
VERSION_NUMBER = "0.1.0"
# Group identifier for your projects
GROUP = "adis"
COPYRIGHT = ""

# Specify Maven 2.0 remote repositories here, like this:
repositories.remote << 'http://www.ibiblio.org/maven2'
repositories.remote << 'http://repo1.maven.org/maven2'
repositories.remote << 'http://repository.codehaus.org'
repositories.remote << 'http://google-gson.googlecode.com/svn/mavenrepo'
repositories.remote << 'http://guiceyfruit.googlecode.com/svn/repo/releases'
repositories.remote << 'http://download.java.net/maven/2'
repositories.remote << 'http://static.appfuse.org/repository'
repositories.remote << 'http://repository.jboss.org/maven2'
repositories.remote << 'http://google-maven-repository.googlecode.com/svn/repository'
repositories.remote << 'http://people.apache.org/repo/m2-incubating-repository'
repositories.remote << 'http://repository.jboss.org/nexus/content/groups/public'
repositories.remote << 'http://repo.marketcetera.org/maven'

SLF4J = transitive('org.slf4j:slf4j-log4j12:jar:1.5.8')
JCMD = transitive('com.beust:jcommander:jar:1.19')
SPRING_VERSION = "3.0.5.RELEASE"
SPRING = [ 
           "org.springframework:spring-asm:jar:#{SPRING_VERSION}",
           "org.springframework:spring-core:jar:#{SPRING_VERSION}",
           "org.springframework:spring-beans:jar:#{SPRING_VERSION}",
           "org.springframework:spring-context:jar:#{SPRING_VERSION}",
           "org.springframework:spring-expression:jar:#{SPRING_VERSION}",
           "org.springframework:spring-oxm:jar:#{SPRING_VERSION}",
           "org.springframework:spring-orm:jar:#{SPRING_VERSION}",
           "org.springframework:spring-jdbc:jar:#{SPRING_VERSION}",
           "org.springframework:spring-web:jar:#{SPRING_VERSION}",
           "org.springframework:spring-webmvc:jar:#{SPRING_VERSION}",
           "org.springframework:spring-expression:jar:#{SPRING_VERSION}",
           "org.springframework:spring-asm:jar:#{SPRING_VERSION}",
           "org.springframework:spring-aop:jar:#{SPRING_VERSION}",
           "org.springframework:spring-aspects:jar:#{SPRING_VERSION}",
           "org.springframework:spring-instrument:jar:#{SPRING_VERSION}",
         ] 
#COMMONS_LOGGING = 'org.slf4j:jcl-over-slf4j:jar:1.6.3'
COMMONS_LOGGING = 'commons-logging:commons-logging:jar:1.1.1'
XSTREAM = transitive('com.thoughtworks.xstream:xstream:jar:1.3.1')
SERVLET = 'javax.servlet:servlet-api:jar:2.5'

desc "AdvancedDiscoveryService"
define "adis" do
        project.version = VERSION_NUMBER
        project.group = GROUP
        manifest["Implementation-Vendor"] = COPYRIGHT

        compile.with SLF4J, JCMD, VOLD_COMMON, VOLD_CLIENT, SPRING, COMMONS_LOGGING, XSTREAM, SERVLET
        mainClass='de.zib.adis.Main'

        package(:jar).with :manifest=>manifest.merge('Main-Class'=>mainClass)
        package(:jar).include _('etc/*'), :path => ''
        package(:jar).include _('src/main/java/META-INF/*'), :path => 'META-INF/'

        package(:war).with :manifest=>manifest.merge('Main-Class'=>mainClass)
        package(:war).include _('etc/*'), :path => 'WEB-INF/classes/'

        desc "Test runs"
        task 'test1' do
                jars = compile.dependencies.map(&:to_s)
                jars += [project.package(:jar).to_s]
                args = ['--baseurl', 'http://localhost:8080/vold', "setDMS", "test"]

                Commands.java(mainClass,
                        args, { :classpath => jars, :verbose => true } )
        end
end

# vim:ft=ruby
