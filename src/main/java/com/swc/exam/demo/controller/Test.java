package com.swc.exam.demo.controller;

public class Test {

	public static void main(String[] args) {
		사람 a사람 = new 사람();
		a사람.숨쉬다();

	}

}


class 동물{
	void 숨쉬다() {
		System.out.println("동물이 숨쉬다");
	}
}
class 사람 extends 동물{
}