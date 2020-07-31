.class public VarNotInit
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 4

	iconst_2
	istore_3

	iconst_1
	istore_2

	iload_2
	iload_3
	iadd
	istore_1

	return
.end method


