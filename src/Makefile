JFLAGS = -cp /usr/local/BerkeleyDB.6.1/lib/db.jar:.
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		MainClass.java \
		UIO.java \
		SimpleUIO.java \
		KVPair.java \
		Structure.java \
		IndexFile.java \
		BTree.java\
		Hash.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class