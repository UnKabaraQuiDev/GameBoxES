package lu.kbra.gamebox.client.es.engine.utils.bake.openal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import org.joml.Math;
import org.lwjgl.system.MemoryUtil;

import lu.kbra.gamebox.client.es.engine.utils.MathUtils;;

public class SoundGenerator {

	public static final float notes[] = new float[8];
	static {
		float start = 440;
		notes[0] = start;
		for (int i = 1; i < 8; i++) {
			start *= 1.057;
			notes[i] = start;
		}
	}

	// Function to compute the Fourier series value for a given x
	public static double computeFourierSeries(double x, double[] radii) {
		double sum = 0;
		for (int i = 0; i < radii.length; i++) {
			double radius = radii[i];
			sum += radius * Math.sin(2 * Math.PI * (i + 1) * x);
		}
		return sum;
	}

	public static void fillBuffer(ShortBuffer buffer, double seconds, int sampleRate) {
		// Constants for the Fourier series
		double[] radii = { 1.0, 0.8, 0.5 };
		double[] coefficients = { 400.0, 200.0, 300.0 };

		for (double second = 0; second < seconds; second++) {
			// Fill for half of the note
			for (int moment = 0; moment < sampleRate / 2; moment++) {
				double time = (second * sampleRate + moment) * Math.PI * 2;

				double x = 0, y = 0;
				double amplitude = 0;
				for (int circle = 0; circle < radii.length; circle++) {
					double radius = radii[circle];
					double coefficient = coefficients[circle];

					x += Math.cos(time) * radius;
					y += Math.sin(time) * radius;
				}

				amplitude = computeFourierSeries(time, radii);

				float angle = (float) java.lang.Math.atan2(x, y);
				int note = (int) (MathUtils.snap(angle, (float) (Math.PI * 2 / 8)) / 8);

				double noteFrequency = notes[note];

				System.out.println("amplitude: " + (time/sampleRate) + " " + amplitude + " : " + (Math.sin(noteFrequency * (time/sampleRate)) * Short.MAX_VALUE));

				buffer.put((short) (Math.sin(noteFrequency * (time/sampleRate)) * Short.MAX_VALUE));
			}

			buffer.position(buffer.position() + sampleRate / 2);

			// Silence for the remaining half of the second
			/*
			 * for (int i = 0; i < sampleRate / 2; i++) { buffer.put((short) 0); }
			 */
		}

		/*
		 * // Calculate the number of samples for 10 seconds int numSamples = sampleRate
		 * * 10;
		 * 
		 * // Generate audio samples for 10 seconds for (int second = 0; second < 10;
		 * second++) { // Generate the note for (int i = 0; i < sampleRate / 2; i++) {
		 * // Calculate time in seconds double time = ((double) second / (double)
		 * sampleRate) * i;
		 * 
		 * // Calculate angle based on time double angle = 2 * Math.PI * time;
		 * 
		 * // Initialize amplitude double amplitude = 0.0;
		 * 
		 * // Calculate amplitude using Fourier series for (int ji = 0; ji <
		 * radii.length; ji++) { amplitude += radii[ji] * Math.cos(coefficients[ji] *
		 * angle + 1); }
		 * 
		 * // Calculate frequency based on angle double frequency = 440 *
		 * java.lang.Math.pow(1.059, angle);
		 * 
		 * // Calculate amplitude-adjusted sample value short sampleValue = (short)
		 * (amplitude * Short.MAX_VALUE / 500);
		 * 
		 * buffer.put(sampleValue); }
		 * 
		 * // Silence for the remaining half of the second for (int i = 0; i <
		 * sampleRate / 2; i++) { buffer.put((short) 0); } }
		 */

		// Reset position of the buffer to prepare for playback
		buffer.flip();
	}

	public static ShortBuffer create() {
		// Define the buffer size
		int bufferSize = 44100 * 10; // 10 seconds at 44100Hz

		int sampleRate = 44100;
		int numSamples = sampleRate * 10;

		// Create a ShortBuffer with the specified size
		ShortBuffer buffer = MemoryUtil.memAllocShort(bufferSize); // ShortBuffer.allocate(bufferSize);

		// Fill the buffer with audio samples
		fillBuffer(buffer, 10, sampleRate);

		return buffer;
	}

	public static void main(String[] args) throws IOException {

		ShortBuffer buffer = create();

		ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.capacity() * 2); // Each short is 2 bytes

		while (buffer.hasRemaining()) {
			byteBuffer.putShort(buffer.get());
		}

		// Write ByteBuffer to file
		File file = new File("./resources/bakes/audio/test.ogg");
		file.getParentFile().mkdirs();
		file.createNewFile();

		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			byteBuffer.flip(); // Prepare for reading
			while (byteBuffer.hasRemaining()) {
				outputStream.write(byteBuffer.get());
			}
			System.out.println("Audio file written successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * Now you can use the buffer for audio playback try (MemoryStack stack =
		 * MemoryStack.stackPush()) { IntBuffer errorBuffer = stack.mallocInt(256);
		 * 
		 * // Initialize the file for writing long file =
		 * STBVorbis.stb_vorbis_open_filename("./resources/bakes/audio/test.ogg",
		 * errorBuffer, null); if (file == MemoryUtil.NULL) {
		 */
		/**
		 * throw new RuntimeException( "Failed to open Ogg file. Error: " +
		 * STBVorbis.stb_vorbis_error(errorBuffer));
		 */
		/*
		 * }
		 * 
		 * // Create an empty VorbisInfo instance STBVorbisInfo info =
		 * STBVorbisInfo.malloc();
		 * 
		 * // Get the file information STBVorbis.stb_vorbis_get_info(file, info);
		 * 
		 * // Write the audio data to the file STBVorbis.stb_vorbis_encode_float(file,
		 * sampleRate, 1, sampleRate);
		 * 
		 * // Close the file STBVorbis.stb_vorbis_close(file);
		 * 
		 * // Free the memory allocated for VorbisInfo info.free(); }
		 */
	}

}
