#!/usr/bin/env python

import subprocess
import os
import os.path

TOMCAT_HOME = '/var/lib/tomcat7'

def main():
    #subprocess.check_call(['bash', TOMCAT_HOME + '/bin/shutdown.sh'])

    subprocess.check_call(['mvn', 'clean'])
    subprocess.check_call(['mvn', 'package'])

    #subprocess.check_call(['rm', '-fr', TOMCAT_HOME + '/webapps/ROOT'])

    #subprocess.check_call(['cp', '-r', 'target/CloudMenuServer', TOMCAT_HOME + '/webapps/ROOT'])
    subprocess.check_call(['cp', 'lib/thrift.jar', 'target/CloudMenuServer/WEB-INF/lib/'])
    subprocess.check_call(['cp', 'lib/sqlite/libsqlite4java-linux-amd64.so', 'target/CloudMenuServer/WEB-INF/lib/'])

    subprocess.check_call(['rm', '-fr', os.path.abspath('target/CloudMenuServer/WEB-INF/view')])
    subprocess.check_call(['ln', '-s', os.path.abspath('src/main/webapp/WEB-INF/view'),
      os.path.abspath('target/CloudMenuServer/WEB-INF/view')])
    #subprocess.check_call(['bash', TOMCAT_HOME + '/bin/startup.sh'])

if __name__ == '__main__':
    main()
