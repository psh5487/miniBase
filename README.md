# minibase
SWE3033 데이터베이스 실습 - MiniBase

## minibase Structure
### Logical Abstraction
<img width="600" src="https://user-images.githubusercontent.com/26567880/91587109-48da0980-e991-11ea-8663-da171cb002ed.png">

### Physical Abstraction (Page, File)
<img width="800" src="https://user-images.githubusercontent.com/26567880/91587122-4e375400-e991-11ea-8f77-1a35855523ba.png">

### Architecture
<img width="550" src="https://user-images.githubusercontent.com/26567880/91587416-b423db80-e991-11ea-9616-fe1dc0be433c.png">

## Lab Assignment
1. Fields, Tuples, Catalog
2. BufferPool
3. HeapFile Mutability, Sequential Scan, Insertion and Deletion, Eviction
4. Filter, Predicate
5. Join
6. Aggregator
7. Lock
8. Join Optimizer, Table, Stats, Histogram

### Development Environment 
- Linux 

### Prerequisite
#### java
```bash
sudo apt-get install openjdk-[version]-jdk
```

#### ant 
what is ant? [link](http://ant.apache.org/) 
```bash
$ sudo apt-get install ant 
```

## Command
|command|description|
|----------|-----------------------------------------|
|ant|Build miniBase|
|ant test|Run all the unit test provided|
|ant runtest -Dtest=testname|Run the specific unit test|
|ant systemtest|Run all the unit test provided|
|ant run systest -Dtest=testname|Run the specific unit test|
|ant clean|Remove binary files|
|and handin|Make tarball|



