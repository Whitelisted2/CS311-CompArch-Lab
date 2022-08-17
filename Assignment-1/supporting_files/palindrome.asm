	.data
a:
	11
	.text
main:
	load %x0, $a, %x3
	addi %x0, 0, %x4
	add %x0, %x3, %x5
loop:
	divi %x5, 10, %x5
	muli %x4, 10, %x4
	add %x4, %x31, %x4
	bne %x5, %x0, loop
out:
	beq %x4, %x3, yespal
	subi %x0, 1, %x10
	end
yespal:
	addi %x0, 1, %x10
	end