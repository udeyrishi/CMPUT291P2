JCC = javac
JFLAGS = -cp /usr/local/BerkeleyDB.6.1/lib/db.jar:.
default: MainClass UIO SimpleUIO KVPair Structure \
		IndexFile BTree Hash

MainClass: MainClass.java
		$(JCC) $(JFLAGS) MainClass.java
UIO: UIO.java
		$(JCC) $(JFLAGS) UIO.java
SimpleUIO: SimpleUIO.java
		$(JCC) $(JFLAGS) SimpleUIO.java
KVPair: KVPair.java
			$(JCC) $(JFLAGS) KVPair.java
Structure: Structure.java
			$(JCC) $(JFLAGS) Structure.java
IndexFile: IndexFile.java
			$(JCC) $(JFLAGS) IndexFile.java
BTree: BTree.java
			$(JCC) $(JFLAGS) BTree.java
Hash: Hash.java
			$(JCC) $(JFLAGS) Hash.java

clean:
		$(RM) *.class