.class public Custom
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public add(II)I
	.limit stack 2
	.limit locals 4

	iload_1
	iload_2
	iadd
	istore_3

	iload_3
	ireturn
.end method


.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 6

	bipush 20
	istore_1

	bipush 10
	istore_2

while_1_begin:
	iload_2
	iload_1
	if_icmpge while_1_end
	iload_1
	iload_1
	iadd
	istore_2

while_2_begin:
	iconst_0
	ifeq while_2_end
	iload_2
	istore_1

	goto while_2_begin
while_2_end:
	goto while_1_begin
while_1_end:
	return
.end method


