.class public ConstPropOpt
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 3

	iconst_0
	istore_2

	bipush 20
	istore_1

while_1_begin:
	iload_2
	iload_1
	if_icmpge while_1_end
	iload_2
	invokestatic io/println(I)V

	iinc 2 1

	goto while_1_begin
while_1_end:
	return
.end method


