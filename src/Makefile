DBJARPATH = /usr/share/java/db.jar

JFLAGS = -cp $(DBJARPATH):.
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		MainClass.java \
		MainClassHelper.java \
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
