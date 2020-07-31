.class public Child
.super Person

.method public <init>()V
	aload_0
	invokenonvirtual Person/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 2

	new Child
	dup
	invokespecial Child/<init>()V
	astore_1

	aload_1
	invokevirtual Child/printInfo()V

	return
.end method


