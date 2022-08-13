	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	addi %x0, 0, %x10
	divi %x3, 2, %x4
	beq %x31, %x0, seteven
	addi %x10, 1, %x10
	end
seteven:
	subi %x10, 1, %x10
	end