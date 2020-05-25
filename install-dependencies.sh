#!/bin/sh
git clone https://github.com/SeineEloquenz/lbcfs.git
cd lbcfs/ || exit
mvn install
git clone https://github.com/SeineEloquenz/lbcfs-plugin-annotations.git
cd lbcfs-plugin-annotations/ || exit
mvn install