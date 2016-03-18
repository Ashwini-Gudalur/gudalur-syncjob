#!/bin/bash
CRONGREP=`crontab -l | grep scripts/run.sh`

if [ "$CRONGREP" != "" ];
then
echo "Crontab already exists"
exit
fi
# Create Command line
BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo BASEDIR "$BASEDIR"
CRONLINE="0 * * * * ${BASEDIR}/../scripts/run.sh"
CRONCOMMFILE=/tmp/crontab.out
#
# Added commands into the file to create the crontab entry
#
export backup_date=`date +20%y%m%d-%H%M%S`
crontab -l > /tmp/crontab.${backup_date}
# # create a copy of the crontab to edit
crontab -l > $CRONCOMMFILE
echo "$CRONLINE" >> $CRONCOMMFILE
crontab $CRONCOMMFILE
echo "Crontab Entry Inserted Successfully"
#