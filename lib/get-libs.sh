#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

wget http://www.stud.fit.vutbr.cz/~xjanot01/images.tgz -P $DIR
tar -xvzf $DIR/images.tgz -C $DIR

rm -f $DIR/images.tgz
