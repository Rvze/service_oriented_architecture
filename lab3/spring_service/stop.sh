#!/bin/bash
procs=$(ps aux | grep "spring_service" | awk '{print $2}')
echo $procs
for i in $procs; do
  echo "kill $i"
  kill $i 2>/dev/null
done
