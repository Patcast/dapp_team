for i in {1.100}
do
  nohup java -cp bin test.TrafficGen 100 &
  echo hello
done