package lu.pcy113.pdr.engine.utils.bake.openal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import org.joml.Math;

public class SoundGenerator {

	public static void fillBuffer(ShortBuffer buffer, int sampleRate) {
		// Constants for the Fourier series
		double[] radii = { 1.0, 0.5 };
		double[] coefficients = { 2.0, 5.0 };

		// Calculate the number of samples for 10 seconds
		int numSamples = sampleRate * 10;

		// Generate audio samples for 10 seconds
		for (int second = 0; second < 10; second++) {
			// Generate the note
			for (int i = 0; i < sampleRate / 2; i++) {
				// Calculate time in seconds
				double time = ((double) second / (double) sampleRate) * i;

				// Calculate angle based on time
				double angle = 2 * Math.PI * time;

				// Initialize amplitude
				double amplitude = 0.0;

				// Calculate amplitude using Fourier series
				for (int ji = 0; ji < radii.length; ji++) {
					amplitude += radii[ji] * Math.cos(coefficients[ji] * angle);
				}

				// Calculate frequency based on angle
				double frequency = 440 * java.lang.Math.pow(1.059, angle);

				// Calculate amplitude-adjusted sample value
				short sampleValue = (short) (amplitude * Short.MAX_VALUE / 500);

				buffer.put(sampleValue);
			}

			// Silence for the remaining half of the second
			for (int i = 0; i < sampleRate / 2; i++) {
				buffer.put((short) 0);
			}
		}

		// Reset position of the buffer to prepare for playback
		buffer.flip();
	}

	public static void main(String[] args) throws IOException {
		// Define the buffer size
		int bufferSize = 44100 * 10; // 10 seconds at 44100Hz

		int sampleRate = 44100;
		int numSamples = sampleRate * 10;

		// Create a ShortBuffer with the specified size
		ShortBuffer buffer = ShortBuffer.allocate(bufferSize);

		// Fill the buffer with audio samples
		fillBuffer(buffer, sampleRate);

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
