package lu.pcy113.pdr.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

public class SharedPrintStream extends PrintStream {

	private PrintStream main;
	
	public SharedPrintStream(PrintStream out1, OutputStream out2, boolean autoFlush) {
		super(out2, autoFlush);
		main = out1;
	}
	
	@Override
	public void print(boolean b) {
		main.print(b);
		super.print(b);
	}
	@Override
	public void print(char c) {
		main.print(c);
		super.print(c);
	}
	@Override
	public void print(char[] s) {
		main.print(s);
		super.print(s);
	}
	@Override
	public void print(double d) {
		main.print(d);
		super.print(d);
	}
	@Override
	public void print(float f) {
		main.print(f);
		super.print(f);
	}
	@Override
	public void print(int i) {
		main.print(i);
		super.print(i);
	}
	@Override
	public void print(long l) {
		main.print(l);
		super.print(l);
	}
	@Override
	public void print(Object obj) {
		main.print(obj);
		super.print(obj);
	}
	@Override
	public void print(String s) {
		main.print(s);
		super.print(s);
	}
	
	@Override
	public PrintStream printf(Locale l, String format, Object... args) {
		main.printf(l, format, args);
		return super.printf(l, format, args);
	}
	@Override
	public PrintStream printf(String format, Object... args) {
		main.printf(format, args);
		return super.printf(format, args);
	}
	
	@Override
	public void println() {
		main.println();
		super.println();
	}
	@Override
	public void println(boolean x) {
		main.println(x);
		super.println(x);
	}
	@Override
	public void println(char x) {
		main.println(x);
		super.println(x);
	}
	@Override
	public void println(char[] x) {
		main.println(x);
		super.println(x);
	}
	@Override
	public void println(double x) {
		main.println(x);
		super.println(x);
	}
	@Override
	public void println(float x) {
		main.println(x);
		super.println(x);
	}
	@Override
	public void println(int x) {
		main.println(x);
		super.println(x);
	}
	@Override
	public void println(long x) {
		main.println(x);
		super.println(x);
	}
	@Override
	public void println(Object x) {
		main.println(x);
		super.println(x);
	}
	@Override
	public void println(String x) {
		main.println(x);
		super.println(x);
	}
	
	@Override
	public PrintStream append(char c) {
		main.append(c);
		return super.append(c);
	}
	@Override
	public PrintStream append(CharSequence csq) {
		main.append(csq);
		return super.append(csq);
	}
	@Override
	public PrintStream append(CharSequence csq, int start, int end) {
		main.append(csq, start, end);
		return super.append(csq, start, end);
	}
	
	@Override
	public PrintStream format(Locale l, String format, Object... args) {
		main.format(l, format, args);
		return super.format(l, format, args);
	}
	@Override
	public PrintStream format(String format, Object... args) {
		main.format(format, args);
		return super.format(format, args);
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		main.write(b);
		super.write(b);
	}
	@Override
	public void write(byte[] buf, int off, int len) {
		main.write(buf, off, len);
		super.write(buf, off, len);
	}
	@Override
	public void write(int b) {
		main.write(b);
		super.write(b);
	}
	
	@Override
	public void flush() {
		//main.flush();
		super.flush();
	}
	
	public void flushSecondary() {
		super.flush();
	}
	public void flushMain() {
		main.flush();
	}
	
	@Override
	public void close() {
		main.close();
		super.close();
	}
	
	public void closeSecondary() {
		super.close();
	}
	public void closeMain() {
		main.close();
	}
	
}
