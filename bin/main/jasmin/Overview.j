.class public Overview
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 5

	iconst_4
	istore 4

	iconst_5
	newarray int
	astore_1

	iconst_0
	istore_2

	new PrintArray
	dup
	invokespecial PrintArray/<init>()V
	astore_3

while_1_begin:
	iload_2
	aload_1
	arraylength
	if_icmpge while_1_end
	aload_1
	iload_2
	iconst_0
	iastore

	iinc 2 1

	goto while_1_begin
while_1_end:
	aload_3
	aload_1
	invokevirtual PrintArray/printArray([I)V

	aload_1
	iconst_1
	iconst_2
	iastore

	aload_3
	aload_1
	invokevirtual PrintArray/printArray([I)V

	aload_1
	iconst_4
	bipush 8
	iastore

	iconst_0
	istore_2

while_2_begin:
	iload_2
	aload_1
	arraylength
	if_icmpge while_2_end
	iload_2
	aload_1
	iload_2
	iaload
	if_icmpge if_3_else
	iload_2
	iload 4
	if_icmpge if_4_else
	aload_1
	iload_2
	bipush 10
	iastore

	goto if_4_end
if_4_else:
if_4_end:
	goto if_3_end
if_3_else:
if_3_end:
	iinc 2 1

	goto while_2_begin
while_2_end:
	aload_3
	aload_1
	invokevirtual PrintArray/printArray([I)V

	iconst_0
	istore_2

while_5_begin:
	aload_1
	iload_2
	iaload
	iload_2
	if_icmpge while_5_end
	aload_1
	iload_2
	bipush 9
	iconst_1
	iadd
	iastore

	iinc 2 1

	goto while_5_begin
while_5_end:
	aload_3
	aload_1
	invokevirtual PrintArray/printArray([I)V

	return
.end method


