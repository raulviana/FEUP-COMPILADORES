.class public Array
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 3
	.limit locals 4

	bipush 100
	istore_2

	bipush 12
	istore_3

	iload_2
	newarray int
	astore_1

	aload_1
	bipush 10
	iload_3
	iastore

	aload_1
	bipush 11
	iaload
	istore_3

	return
.end method


