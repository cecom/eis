#!/bin/bash

INITIAL_CALL="$0 $@"
INITIAL_ARGNUMBER=$#

SCRIPT_COMPLETE=$0
SCRIPT_NAME=`basename $0`
SCRIPT_PATH=`dirname $0`

readParams(){
  EIS_JAR=$SCRIPT_PATH/target/eis-1.0-SNAPSHOT.jar
  NEO_URL="http://localhost:7474/db/data"
  PROPERTY_FOLDER=/c/propertyfiles/

  while [ $# -gt 0 ] ; do
    key=`echo "$1"   | awk -F= '{print $1}'`
    value=`echo "$1" | awk -F= '{print $2}'`
    case "$key" in
	     --eisJar)
            EIS_JAR=$value
            ;; 
        --neoUrl)
            NEO_URL=$value
            ;;
        --neoDb)
            NEO_DB=$value
            ;;
        --propertyFolder)
            PROPERTY_FOLDER=$value
            ;;
    esac
    shift
  done

}

executeEis(){
    eval "java -jar $EIS_JAR $*"
    if [ $? -ne 0 ] ; then
       echo "We got an error while executing eis with params [$*]"
       exit 1
    fi
}

#read the params
readParams $*

#delete all
echo "== Delete all"
executeEis delete-all --url="$NEO_URL"

#import deployments
echo "== Import Deployments"
executeEis create-deployment --url="$NEO_URL" --name="ALL_DEPLOYMENTS"

#import environments
echo "== Import Environments"
executeEis create-environment --url="$NEO_URL" --name="ALL_ENVIRONMENTS"

for environment in `cd $PROPERTY_FOLDER && ls -1  | sed -e 's#\.properties##'"` ; do
    parentEnvironment=`echo $environment | sed -e 's#[0-9]*##g'`
    if [ `cd $PROPERTY_FOLDER && ls -1 $parentEnvironment* | wc -l` -gt 1 ] ; then
         if [ $environment == "${parentEnvironment}1" ] ; then
            executeEis create-environment --url="$NEO_URL" --name="$parentEnvironment"  --basedOn="ALL_ENVIRONMENTS"
         fi
    else
       parentEnvironment="ALL_ENVIRONMENTS"
    fi
    executeEis create-environment --url="$NEO_URL" --name="$environment"     --basedOn="$parentEnvironment"
done

#import properties
echo "== Import Properties"
for property in `dos2unix < $PROPERTY_FOLDER/_base.properties | grep -v "^$" | grep -v "^#" | awk -F= '{print $1}'`; do
   executeEis create-property --url="$NEO_URL" --wasIntroduced="ALL_DEPLOYMENTS" --temporalScope=LATEST --name="$property"
done

#import property values
echo "== Import Property Values"
echo "========= [_base.properties] ======================"
dos2unix < $PROPERTY_FOLDER/_base.properties | while read line
do
   if [ `echo "$line" | grep "^#\|^$" | wc -l` -eq 1 ] ; then
      continue
   fi
   propertyKey=`echo "$line" | awk -F= '{print $1}'`
   propertyValue=`echo "$line" | cut --delimiter="=" -f2-`

   executeEis set-value \
       --url="$NEO_URL" \
       --property="$propertyKey"  \
       --deployment="ALL_DEPLOYMENTS"        \
       --environment="ALL_ENVIRONMENTS"    \
       --value=\"$propertyValue\"
done

for environment in `cd $PROPERTY_FOLDER && ls -1  | grep -v "_base"` ; do
   echo "========= [$environment] ======================"
   dos2unix < $PROPERTY_FOLDER/$environment | while read line
   do
      if [ `echo "$line" | grep "^#\|^$" | wc -l` -eq 1 ] ; then
         continue
      fi

      envname=`echo $environment | sed -e 's#.properties##'`

      propertyKey=`echo "$line" | awk -F= '{print $1}'`
      propertyValue=`echo "$line" | cut --delimiter="=" -f2-`
      executeEis set-value \
          --url="$NEO_URL" \
          --property="$propertyKey"  \
          --deployment="ALL_DEPLOYMENTS"        \
          --environment="$envname"    \
          --value=\"$propertyValue\"
   done
done
