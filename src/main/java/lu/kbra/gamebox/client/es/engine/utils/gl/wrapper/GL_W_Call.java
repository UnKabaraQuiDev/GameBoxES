package lu.kbra.gamebox.client.es.engine.utils.gl.wrapper;

public interface GL_W_Call {
	void glReadBuffer(int arg0);

	void nglDrawRangeElements(int arg0, int arg1, int arg2, int arg3, int arg4, long arg5);

	void glDrawRangeElements(int arg0, int arg1, int arg2, java.nio.ByteBuffer arg3);

	void glDrawRangeElements(int arg0, int arg1, int arg2, int arg3, java.nio.ByteBuffer arg4);

	void glDrawRangeElements(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	void glDrawRangeElements(int arg0, int arg1, int arg2, java.nio.ShortBuffer arg3);

	void glDrawRangeElements(int arg0, int arg1, int arg2, int arg3, int arg4, long arg5);

	void nglTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, long arg9);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, java.nio.FloatBuffer arg9);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, java.nio.IntBuffer arg9);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int[] arg9);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, float[] arg9);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, java.nio.ByteBuffer arg9);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, short[] arg9);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, java.nio.ShortBuffer arg9);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, long arg9);

	void nglTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, long arg10);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, short[] arg10);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int[] arg10);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, float[] arg10);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, java.nio.ByteBuffer arg10);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, long arg10);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, java.nio.ShortBuffer arg10);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, java.nio.IntBuffer arg10);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, java.nio.FloatBuffer arg10);

	void glCopyTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8);

	void nglCompressedTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8);

	void glCompressedTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8);

	void glCompressedTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, java.nio.ByteBuffer arg7);

	void nglCompressedTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, long arg10);

	void glCompressedTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, long arg10);

	void glCompressedTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, java.nio.ByteBuffer arg9);

	void nglGenQueries(int arg0, long arg1);

	void glGenQueries(java.nio.IntBuffer arg0);

	void glGenQueries(int[] arg0);

	int glGenQueries();

	void nglDeleteQueries(int arg0, long arg1);

	void glDeleteQueries(int[] arg0);

	void glDeleteQueries(int arg0);

	void glDeleteQueries(java.nio.IntBuffer arg0);

	boolean glIsQuery(int arg0);

	void glBeginQuery(int arg0, int arg1);

	void glEndQuery(int arg0);

	void nglGetQueryiv(int arg0, int arg1, long arg2);

	void glGetQueryiv(int arg0, int arg1, int[] arg2);

	void glGetQueryiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	int glGetQueryi(int arg0, int arg1);

	void nglGetQueryObjectuiv(int arg0, int arg1, long arg2);

	void glGetQueryObjectuiv(int arg0, int arg1, int[] arg2);

	void glGetQueryObjectuiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	int glGetQueryObjectui(int arg0, int arg1);

	boolean glUnmapBuffer(int arg0);

	void nglGetBufferPointerv(int arg0, int arg1, long arg2);

	void glGetBufferPointerv(int arg0, int arg1, org.lwjgl.PointerBuffer arg2);

	long glGetBufferPointer(int arg0, int arg1);

	void nglDrawBuffers(int arg0, long arg1);

	void glDrawBuffers(int arg0);

	void glDrawBuffers(int[] arg0);

	void glDrawBuffers(java.nio.IntBuffer arg0);

	void nglUniformMatrix2x3fv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix2x3fv(int arg0, boolean arg1, float[] arg2);

	void glUniformMatrix2x3fv(int arg0, boolean arg1, java.nio.FloatBuffer arg2);

	void nglUniformMatrix3x2fv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix3x2fv(int arg0, boolean arg1, float[] arg2);

	void glUniformMatrix3x2fv(int arg0, boolean arg1, java.nio.FloatBuffer arg2);

	void nglUniformMatrix2x4fv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix2x4fv(int arg0, boolean arg1, float[] arg2);

	void glUniformMatrix2x4fv(int arg0, boolean arg1, java.nio.FloatBuffer arg2);

	void nglUniformMatrix4x2fv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix4x2fv(int arg0, boolean arg1, float[] arg2);

	void glUniformMatrix4x2fv(int arg0, boolean arg1, java.nio.FloatBuffer arg2);

	void nglUniformMatrix3x4fv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix3x4fv(int arg0, boolean arg1, java.nio.FloatBuffer arg2);

	void glUniformMatrix3x4fv(int arg0, boolean arg1, float[] arg2);

	void nglUniformMatrix4x3fv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix4x3fv(int arg0, boolean arg1, java.nio.FloatBuffer arg2);

	void glUniformMatrix4x3fv(int arg0, boolean arg1, float[] arg2);

	void glBlitFramebuffer(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9);

	void glRenderbufferStorageMultisample(int arg0, int arg1, int arg2, int arg3, int arg4);

	void glFramebufferTextureLayer(int arg0, int arg1, int arg2, int arg3, int arg4);

	long nglMapBufferRange(int arg0, long arg1, long arg2, int arg3);

	java.nio.ByteBuffer glMapBufferRange(int arg0, long arg1, long arg2, int arg3);

	java.nio.ByteBuffer glMapBufferRange(int arg0, long arg1, long arg2, int arg3, java.nio.ByteBuffer arg4);

	void glFlushMappedBufferRange(int arg0, long arg1, long arg2);

	void glBindVertexArray(int arg0);

	void nglDeleteVertexArrays(int arg0, long arg1);

	void glDeleteVertexArrays(int[] arg0);

	void glDeleteVertexArrays(int arg0);

	void glDeleteVertexArrays(java.nio.IntBuffer arg0);

	void nglGenVertexArrays(int arg0, long arg1);

	int glGenVertexArrays();

	void glGenVertexArrays(int[] arg0);

	void glGenVertexArrays(java.nio.IntBuffer arg0);

	boolean glIsVertexArray(int arg0);

	void nglGetIntegeri_v(int arg0, int arg1, long arg2);

	void glGetIntegeri_v(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetIntegeri_v(int arg0, int arg1, int[] arg2);

	int glGetIntegeri(int arg0, int arg1);

	void glBeginTransformFeedback(int arg0);

	void glEndTransformFeedback();

	void glBindBufferRange(int arg0, int arg1, int arg2, long arg3, long arg4);

	void glBindBufferBase(int arg0, int arg1, int arg2);

	void nglTransformFeedbackVaryings(int arg0, int arg1, long arg2, int arg3);

	void glTransformFeedbackVaryings(int arg0, java.lang.CharSequence[] arg1, int arg2);

	void glTransformFeedbackVaryings(int arg0, java.lang.CharSequence arg1, int arg2);

	void glTransformFeedbackVaryings(int arg0, org.lwjgl.PointerBuffer arg1, int arg2);

	void nglGetTransformFeedbackVarying(int arg0, int arg1, int arg2, long arg3, long arg4, long arg5, long arg6);

	void glGetTransformFeedbackVarying(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4, java.nio.ByteBuffer arg5);

	java.lang.String glGetTransformFeedbackVarying(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4);

	java.lang.String glGetTransformFeedbackVarying(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3);

	void glGetTransformFeedbackVarying(int arg0, int arg1, int[] arg2, int[] arg3, int[] arg4, java.nio.ByteBuffer arg5);

	void nglVertexAttribIPointer(int arg0, int arg1, int arg2, int arg3, long arg4);

	void glVertexAttribIPointer(int arg0, int arg1, int arg2, int arg3, java.nio.ShortBuffer arg4);

	void glVertexAttribIPointer(int arg0, int arg1, int arg2, int arg3, long arg4);

	void glVertexAttribIPointer(int arg0, int arg1, int arg2, int arg3, java.nio.ByteBuffer arg4);

	void glVertexAttribIPointer(int arg0, int arg1, int arg2, int arg3, java.nio.IntBuffer arg4);

	void nglGetVertexAttribIiv(int arg0, int arg1, long arg2);

	void glGetVertexAttribIiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetVertexAttribIiv(int arg0, int arg1, int[] arg2);

	int glGetVertexAttribIi(int arg0, int arg1);

	void nglGetVertexAttribIuiv(int arg0, int arg1, long arg2);

	void glGetVertexAttribIuiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetVertexAttribIuiv(int arg0, int arg1, int[] arg2);

	int glGetVertexAttribIui(int arg0, int arg1);

	void glVertexAttribI4i(int arg0, int arg1, int arg2, int arg3, int arg4);

	void glVertexAttribI4ui(int arg0, int arg1, int arg2, int arg3, int arg4);

	void nglVertexAttribI4iv(int arg0, long arg1);

	void glVertexAttribI4iv(int arg0, int[] arg1);

	void glVertexAttribI4iv(int arg0, java.nio.IntBuffer arg1);

	void nglVertexAttribI4uiv(int arg0, long arg1);

	void glVertexAttribI4uiv(int arg0, int[] arg1);

	void glVertexAttribI4uiv(int arg0, java.nio.IntBuffer arg1);

	void nglGetUniformuiv(int arg0, int arg1, long arg2);

	void glGetUniformuiv(int arg0, int arg1, int[] arg2);

	void glGetUniformuiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	int glGetUniformui(int arg0, int arg1);

	int nglGetFragDataLocation(int arg0, long arg1);

	int glGetFragDataLocation(int arg0, java.nio.ByteBuffer arg1);

	int glGetFragDataLocation(int arg0, java.lang.CharSequence arg1);

	void glUniform1ui(int arg0, int arg1);

	void glUniform2ui(int arg0, int arg1, int arg2);

	void glUniform3ui(int arg0, int arg1, int arg2, int arg3);

	void glUniform4ui(int arg0, int arg1, int arg2, int arg3, int arg4);

	void nglUniform1uiv(int arg0, int arg1, long arg2);

	void glUniform1uiv(int arg0, java.nio.IntBuffer arg1);

	void glUniform1uiv(int arg0, int[] arg1);

	void nglUniform2uiv(int arg0, int arg1, long arg2);

	void glUniform2uiv(int arg0, java.nio.IntBuffer arg1);

	void glUniform2uiv(int arg0, int[] arg1);

	void nglUniform3uiv(int arg0, int arg1, long arg2);

	void glUniform3uiv(int arg0, int[] arg1);

	void glUniform3uiv(int arg0, java.nio.IntBuffer arg1);

	void nglUniform4uiv(int arg0, int arg1, long arg2);

	void glUniform4uiv(int arg0, java.nio.IntBuffer arg1);

	void glUniform4uiv(int arg0, int[] arg1);

	void nglClearBufferiv(int arg0, int arg1, long arg2);

	void glClearBufferiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glClearBufferiv(int arg0, int arg1, int[] arg2);

	void nglClearBufferuiv(int arg0, int arg1, long arg2);

	void glClearBufferuiv(int arg0, int arg1, int[] arg2);

	void glClearBufferuiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void nglClearBufferfv(int arg0, int arg1, long arg2);

	void glClearBufferfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glClearBufferfv(int arg0, int arg1, float[] arg2);

	void glClearBufferfi(int arg0, int arg1, float arg2, int arg3);

	long nglGetStringi(int arg0, int arg1);

	java.lang.String glGetStringi(int arg0, int arg1);

	void glCopyBufferSubData(int arg0, int arg1, long arg2, long arg3, long arg4);

	void nglGetUniformIndices(int arg0, int arg1, long arg2, long arg3);

	void glGetUniformIndices(int arg0, org.lwjgl.PointerBuffer arg1, int[] arg2);

	void glGetUniformIndices(int arg0, org.lwjgl.PointerBuffer arg1, java.nio.IntBuffer arg2);

	void nglGetActiveUniformsiv(int arg0, int arg1, long arg2, int arg3, long arg4);

	void glGetActiveUniformsiv(int arg0, int[] arg1, int arg2, int[] arg3);

	void glGetActiveUniformsiv(int arg0, java.nio.IntBuffer arg1, int arg2, java.nio.IntBuffer arg3);

	int nglGetUniformBlockIndex(int arg0, long arg1);

	int glGetUniformBlockIndex(int arg0, java.nio.ByteBuffer arg1);

	int glGetUniformBlockIndex(int arg0, java.lang.CharSequence arg1);

	void nglGetActiveUniformBlockiv(int arg0, int arg1, int arg2, long arg3);

	void glGetActiveUniformBlockiv(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	void glGetActiveUniformBlockiv(int arg0, int arg1, int arg2, int[] arg3);

	int glGetActiveUniformBlocki(int arg0, int arg1, int arg2);

	void nglGetActiveUniformBlockName(int arg0, int arg1, int arg2, long arg3, long arg4);

	java.lang.String glGetActiveUniformBlockName(int arg0, int arg1, int arg2);

	void glGetActiveUniformBlockName(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.ByteBuffer arg3);

	void glGetActiveUniformBlockName(int arg0, int arg1, int[] arg2, java.nio.ByteBuffer arg3);

	java.lang.String glGetActiveUniformBlockName(int arg0, int arg1);

	void glUniformBlockBinding(int arg0, int arg1, int arg2);

	void glDrawArraysInstanced(int arg0, int arg1, int arg2, int arg3);

	void nglDrawElementsInstanced(int arg0, int arg1, int arg2, long arg3, int arg4);

	void glDrawElementsInstanced(int arg0, int arg1, int arg2, long arg3, int arg4);

	void glDrawElementsInstanced(int arg0, java.nio.IntBuffer arg1, int arg2);

	void glDrawElementsInstanced(int arg0, java.nio.ShortBuffer arg1, int arg2);

	void glDrawElementsInstanced(int arg0, java.nio.ByteBuffer arg1, int arg2);

	void glDrawElementsInstanced(int arg0, int arg1, java.nio.ByteBuffer arg2, int arg3);

	long glFenceSync(int arg0, int arg1);

	boolean nglIsSync(long arg0);

	boolean glIsSync(long arg0);

	void nglDeleteSync(long arg0);

	void glDeleteSync(long arg0);

	int nglClientWaitSync(long arg0, int arg1, long arg2);

	int glClientWaitSync(long arg0, int arg1, long arg2);

	void nglWaitSync(long arg0, int arg1, long arg2);

	void glWaitSync(long arg0, int arg1, long arg2);

	void nglGetInteger64v(int arg0, long arg1);

	void glGetInteger64v(int arg0, java.nio.LongBuffer arg1);

	void glGetInteger64v(int arg0, long[] arg1);

	long glGetInteger64(int arg0);

	void nglGetSynciv(long arg0, int arg1, int arg2, long arg3, long arg4);

	void glGetSynciv(long arg0, int arg1, int[] arg2, int[] arg3);

	void glGetSynciv(long arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3);

	int glGetSynci(long arg0, int arg1, java.nio.IntBuffer arg2);

	void nglGetInteger64i_v(int arg0, int arg1, long arg2);

	void glGetInteger64i_v(int arg0, int arg1, long[] arg2);

	void glGetInteger64i_v(int arg0, int arg1, java.nio.LongBuffer arg2);

	long glGetInteger64i(int arg0, int arg1);

	void nglGetBufferParameteri64v(int arg0, int arg1, long arg2);

	void glGetBufferParameteri64v(int arg0, int arg1, long[] arg2);

	void glGetBufferParameteri64v(int arg0, int arg1, java.nio.LongBuffer arg2);

	long glGetBufferParameteri64(int arg0, int arg1);

	void nglGenSamplers(int arg0, long arg1);

	void glGenSamplers(java.nio.IntBuffer arg0);

	void glGenSamplers(int[] arg0);

	int glGenSamplers();

	void nglDeleteSamplers(int arg0, long arg1);

	void glDeleteSamplers(int arg0);

	void glDeleteSamplers(java.nio.IntBuffer arg0);

	void glDeleteSamplers(int[] arg0);

	boolean glIsSampler(int arg0);

	void glBindSampler(int arg0, int arg1);

	void glSamplerParameteri(int arg0, int arg1, int arg2);

	void nglSamplerParameteriv(int arg0, int arg1, long arg2);

	void glSamplerParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glSamplerParameteriv(int arg0, int arg1, int[] arg2);

	void glSamplerParameterf(int arg0, int arg1, float arg2);

	void nglSamplerParameterfv(int arg0, int arg1, long arg2);

	void glSamplerParameterfv(int arg0, int arg1, float[] arg2);

	void glSamplerParameterfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void nglGetSamplerParameteriv(int arg0, int arg1, long arg2);

	void glGetSamplerParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetSamplerParameteriv(int arg0, int arg1, int[] arg2);

	int glGetSamplerParameteri(int arg0, int arg1);

	void nglGetSamplerParameterfv(int arg0, int arg1, long arg2);

	void glGetSamplerParameterfv(int arg0, int arg1, float[] arg2);

	void glGetSamplerParameterfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	float glGetSamplerParameterf(int arg0, int arg1);

	void glVertexAttribDivisor(int arg0, int arg1);

	void glBindTransformFeedback(int arg0, int arg1);

	void nglDeleteTransformFeedbacks(int arg0, long arg1);

	void glDeleteTransformFeedbacks(int[] arg0);

	void glDeleteTransformFeedbacks(java.nio.IntBuffer arg0);

	void glDeleteTransformFeedbacks(int arg0);

	void nglGenTransformFeedbacks(int arg0, long arg1);

	void glGenTransformFeedbacks(java.nio.IntBuffer arg0);

	int glGenTransformFeedbacks();

	void glGenTransformFeedbacks(int[] arg0);

	boolean glIsTransformFeedback(int arg0);

	void glPauseTransformFeedback();

	void glResumeTransformFeedback();

	void nglGetProgramBinary(int arg0, int arg1, long arg2, long arg3, long arg4);

	void glGetProgramBinary(int arg0, int[] arg1, int[] arg2, java.nio.ByteBuffer arg3);

	void glGetProgramBinary(int arg0, java.nio.IntBuffer arg1, java.nio.IntBuffer arg2, java.nio.ByteBuffer arg3);

	void nglProgramBinary(int arg0, int arg1, long arg2, int arg3);

	void glProgramBinary(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glProgramParameteri(int arg0, int arg1, int arg2);

	void nglInvalidateFramebuffer(int arg0, int arg1, long arg2);

	void glInvalidateFramebuffer(int arg0, java.nio.IntBuffer arg1);

	void glInvalidateFramebuffer(int arg0, int arg1);

	void glInvalidateFramebuffer(int arg0, int[] arg1);

	void nglInvalidateSubFramebuffer(int arg0, int arg1, long arg2, int arg3, int arg4, int arg5, int arg6);

	void glInvalidateSubFramebuffer(int arg0, int[] arg1, int arg2, int arg3, int arg4, int arg5);

	void glInvalidateSubFramebuffer(int arg0, java.nio.IntBuffer arg1, int arg2, int arg3, int arg4, int arg5);

	void glInvalidateSubFramebuffer(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5);

	void glTexStorage2D(int arg0, int arg1, int arg2, int arg3, int arg4);

	void glTexStorage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5);

	void nglGetInternalformativ(int arg0, int arg1, int arg2, int arg3, long arg4);

	void glGetInternalformativ(int arg0, int arg1, int arg2, int[] arg3);

	void glGetInternalformativ(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	int glGetInternalformati(int arg0, int arg1, int arg2);

	int glGetProgrami(int arg0, int arg1);

	void glActiveTexture(int arg0);

	void glAttachShader(int arg0, int arg1);

	void nglBindAttribLocation(int arg0, int arg1, long arg2);

	void glBindAttribLocation(int arg0, int arg1, java.lang.CharSequence arg2);

	void glBindAttribLocation(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glBindBuffer(int arg0, int arg1);

	void glBindFramebuffer(int arg0, int arg1);

	void glBindRenderbuffer(int arg0, int arg1);

	void glBindTexture(int arg0, int arg1);

	void glBlendColor(float arg0, float arg1, float arg2, float arg3);

	void glBlendEquation(int arg0);

	void glBlendEquationSeparate(int arg0, int arg1);

	void glBlendFunc(int arg0, int arg1);

	void glBlendFuncSeparate(int arg0, int arg1, int arg2, int arg3);

	void nglBufferData(int arg0, long arg1, long arg2, int arg3);

	void glBufferData(int arg0, java.nio.FloatBuffer arg1, int arg2);

	void glBufferData(int arg0, short[] arg1, int arg2);

	void glBufferData(int arg0, int[] arg1, int arg2);

	void glBufferData(int arg0, float[] arg1, int arg2);

	void glBufferData(int arg0, long arg1, int arg2);

	void glBufferData(int arg0, java.nio.ByteBuffer arg1, int arg2);

	void glBufferData(int arg0, java.nio.ShortBuffer arg1, int arg2);

	void glBufferData(int arg0, java.nio.IntBuffer arg1, int arg2);

	void nglBufferSubData(int arg0, long arg1, long arg2, long arg3);

	void glBufferSubData(int arg0, long arg1, java.nio.ByteBuffer arg2);

	void glBufferSubData(int arg0, long arg1, float[] arg2);

	void glBufferSubData(int arg0, long arg1, java.nio.ShortBuffer arg2);

	void glBufferSubData(int arg0, long arg1, int[] arg2);

	void glBufferSubData(int arg0, long arg1, short[] arg2);

	void glBufferSubData(int arg0, long arg1, java.nio.FloatBuffer arg2);

	void glBufferSubData(int arg0, long arg1, java.nio.IntBuffer arg2);

	int glCheckFramebufferStatus(int arg0);

	void glClear(int arg0);

	void glClearColor(float arg0, float arg1, float arg2, float arg3);

	void glClearDepthf(float arg0);

	void glClearStencil(int arg0);

	void glColorMask(boolean arg0, boolean arg1, boolean arg2, boolean arg3);

	void glCompileShader(int arg0);

	void nglCompressedTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, long arg7);

	void glCompressedTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, long arg7);

	void glCompressedTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.ByteBuffer arg6);

	void nglCompressedTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8);

	void glCompressedTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8);

	void glCompressedTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, java.nio.ByteBuffer arg7);

	void glCopyTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7);

	void glCopyTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7);

	int glCreateProgram();

	int glCreateShader(int arg0);

	void glCullFace(int arg0);

	void nglDeleteBuffers(int arg0, long arg1);

	void glDeleteBuffers(int arg0);

	void glDeleteBuffers(java.nio.IntBuffer arg0);

	void glDeleteBuffers(int[] arg0);

	void nglDeleteFramebuffers(int arg0, long arg1);

	void glDeleteFramebuffers(java.nio.IntBuffer arg0);

	void glDeleteFramebuffers(int[] arg0);

	void glDeleteFramebuffers(int arg0);

	void glDeleteProgram(int arg0);

	void nglDeleteRenderbuffers(int arg0, long arg1);

	void glDeleteRenderbuffers(int arg0);

	void glDeleteRenderbuffers(java.nio.IntBuffer arg0);

	void glDeleteRenderbuffers(int[] arg0);

	void glDeleteShader(int arg0);

	void nglDeleteTextures(int arg0, long arg1);

	void glDeleteTextures(int[] arg0);

	void glDeleteTextures(int arg0);

	void glDeleteTextures(java.nio.IntBuffer arg0);

	void glDepthFunc(int arg0);

	void glDepthMask(boolean arg0);

	void glDepthRangef(float arg0, float arg1);

	void glDetachShader(int arg0, int arg1);

	void glDisable(int arg0);

	void glDisableVertexAttribArray(int arg0);

	void glDrawArrays(int arg0, int arg1, int arg2);

	void nglDrawElements(int arg0, int arg1, int arg2, long arg3);

	void glDrawElements(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glDrawElements(int arg0, java.nio.ShortBuffer arg1);

	void glDrawElements(int arg0, int arg1, int arg2, long arg3);

	void glDrawElements(int arg0, java.nio.ByteBuffer arg1);

	void glDrawElements(int arg0, java.nio.IntBuffer arg1);

	void glEnable(int arg0);

	void glEnableVertexAttribArray(int arg0);

	void glFinish();

	void glFlush();

	void glFramebufferRenderbuffer(int arg0, int arg1, int arg2, int arg3);

	void glFramebufferTexture2D(int arg0, int arg1, int arg2, int arg3, int arg4);

	void glFrontFace(int arg0);

	void nglGenBuffers(int arg0, long arg1);

	int glGenBuffers();

	void glGenBuffers(int[] arg0);

	void glGenBuffers(java.nio.IntBuffer arg0);

	void glGenerateMipmap(int arg0);

	void nglGenFramebuffers(int arg0, long arg1);

	int glGenFramebuffers();

	void glGenFramebuffers(java.nio.IntBuffer arg0);

	void glGenFramebuffers(int[] arg0);

	void nglGenRenderbuffers(int arg0, long arg1);

	int glGenRenderbuffers();

	void glGenRenderbuffers(java.nio.IntBuffer arg0);

	void glGenRenderbuffers(int[] arg0);

	void nglGenTextures(int arg0, long arg1);

	int glGenTextures();

	void glGenTextures(java.nio.IntBuffer arg0);

	void glGenTextures(int[] arg0);

	void nglGetActiveAttrib(int arg0, int arg1, int arg2, long arg3, long arg4, long arg5, long arg6);

	java.lang.String glGetActiveAttrib(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3);

	void glGetActiveAttrib(int arg0, int arg1, int[] arg2, int[] arg3, int[] arg4, java.nio.ByteBuffer arg5);

	void glGetActiveAttrib(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4, java.nio.ByteBuffer arg5);

	java.lang.String glGetActiveAttrib(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4);

	void nglGetActiveUniform(int arg0, int arg1, int arg2, long arg3, long arg4, long arg5, long arg6);

	void glGetActiveUniform(int arg0, int arg1, int[] arg2, int[] arg3, int[] arg4, java.nio.ByteBuffer arg5);

	void glGetActiveUniform(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4, java.nio.ByteBuffer arg5);

	java.lang.String glGetActiveUniform(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3);

	java.lang.String glGetActiveUniform(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4);

	void nglGetAttachedShaders(int arg0, int arg1, long arg2, long arg3);

	void glGetAttachedShaders(int arg0, java.nio.IntBuffer arg1, java.nio.IntBuffer arg2);

	void glGetAttachedShaders(int arg0, int[] arg1, int[] arg2);

	int nglGetAttribLocation(int arg0, long arg1);

	int glGetAttribLocation(int arg0, java.lang.CharSequence arg1);

	int glGetAttribLocation(int arg0, java.nio.ByteBuffer arg1);

	void nglGetBooleanv(int arg0, long arg1);

	void glGetBooleanv(int arg0, java.nio.ByteBuffer arg1);

	boolean glGetBoolean(int arg0);

	void nglGetBufferParameteriv(int arg0, int arg1, long arg2);

	void glGetBufferParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetBufferParameteriv(int arg0, int arg1, int[] arg2);

	int glGetBufferParameteri(int arg0, int arg1);

	int glGetError();

	void nglGetFloatv(int arg0, long arg1);

	void glGetFloatv(int arg0, java.nio.FloatBuffer arg1);

	void glGetFloatv(int arg0, float[] arg1);

	float glGetFloat(int arg0);

	void nglGetFramebufferAttachmentParameteriv(int arg0, int arg1, int arg2, long arg3);

	void glGetFramebufferAttachmentParameteriv(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	void glGetFramebufferAttachmentParameteriv(int arg0, int arg1, int arg2, int[] arg3);

	int glGetFramebufferAttachmentParameteri(int arg0, int arg1, int arg2);

	void nglGetIntegerv(int arg0, long arg1);

	void glGetIntegerv(int arg0, int[] arg1);

	void glGetIntegerv(int arg0, java.nio.IntBuffer arg1);

	int glGetInteger(int arg0);

	void nglGetProgramiv(int arg0, int arg1, long arg2);

	void glGetProgramiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetProgramiv(int arg0, int arg1, int[] arg2);

	void nglGetProgramInfoLog(int arg0, int arg1, long arg2, long arg3);

	void glGetProgramInfoLog(int arg0, int[] arg1, java.nio.ByteBuffer arg2);

	java.lang.String glGetProgramInfoLog(int arg0);

	java.lang.String glGetProgramInfoLog(int arg0, int arg1);

	void glGetProgramInfoLog(int arg0, java.nio.IntBuffer arg1, java.nio.ByteBuffer arg2);

	void nglGetRenderbufferParameteriv(int arg0, int arg1, long arg2);

	void glGetRenderbufferParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetRenderbufferParameteriv(int arg0, int arg1, int[] arg2);

	int glGetRenderbufferParameteri(int arg0, int arg1);

	void nglGetShaderiv(int arg0, int arg1, long arg2);

	void glGetShaderiv(int arg0, int arg1, int[] arg2);

	void glGetShaderiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	int glGetShaderi(int arg0, int arg1);

	void nglGetShaderInfoLog(int arg0, int arg1, long arg2, long arg3);

	java.lang.String glGetShaderInfoLog(int arg0);

	void glGetShaderInfoLog(int arg0, int[] arg1, java.nio.ByteBuffer arg2);

	java.lang.String glGetShaderInfoLog(int arg0, int arg1);

	void glGetShaderInfoLog(int arg0, java.nio.IntBuffer arg1, java.nio.ByteBuffer arg2);

	void nglGetShaderPrecisionFormat(int arg0, int arg1, long arg2, long arg3);

	void glGetShaderPrecisionFormat(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3);

	void glGetShaderPrecisionFormat(int arg0, int arg1, int[] arg2, int[] arg3);

	void nglGetShaderSource(int arg0, int arg1, long arg2, long arg3);

	java.lang.String glGetShaderSource(int arg0);

	void glGetShaderSource(int arg0, int[] arg1, java.nio.ByteBuffer arg2);

	java.lang.String glGetShaderSource(int arg0, int arg1);

	void glGetShaderSource(int arg0, java.nio.IntBuffer arg1, java.nio.ByteBuffer arg2);

	long nglGetString(int arg0);

	java.lang.String glGetString(int arg0);

	void nglGetTexParameterfv(int arg0, int arg1, long arg2);

	void glGetTexParameterfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glGetTexParameterfv(int arg0, int arg1, float[] arg2);

	float glGetTexParameterf(int arg0, int arg1);

	void nglGetTexParameteriv(int arg0, int arg1, long arg2);

	void glGetTexParameteriv(int arg0, int arg1, int[] arg2);

	void glGetTexParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2);

	int glGetTexParameteri(int arg0, int arg1);

	void nglGetUniformfv(int arg0, int arg1, long arg2);

	void glGetUniformfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glGetUniformfv(int arg0, int arg1, float[] arg2);

	float glGetUniformf(int arg0, int arg1);

	void nglGetUniformiv(int arg0, int arg1, long arg2);

	void glGetUniformiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetUniformiv(int arg0, int arg1, int[] arg2);

	int glGetUniformi(int arg0, int arg1);

	int nglGetUniformLocation(int arg0, long arg1);

	int glGetUniformLocation(int arg0, java.lang.CharSequence arg1);

	int glGetUniformLocation(int arg0, java.nio.ByteBuffer arg1);

	void nglGetVertexAttribfv(int arg0, int arg1, long arg2);

	void glGetVertexAttribfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glGetVertexAttribfv(int arg0, int arg1, float[] arg2);

	void nglGetVertexAttribiv(int arg0, int arg1, long arg2);

	void glGetVertexAttribiv(int arg0, int arg1, int[] arg2);

	void glGetVertexAttribiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void nglGetVertexAttribPointerv(int arg0, int arg1, long arg2);

	void glGetVertexAttribPointerv(int arg0, int arg1, org.lwjgl.PointerBuffer arg2);

	long glGetVertexAttribPointer(int arg0, int arg1);

	void glHint(int arg0, int arg1);

	boolean glIsBuffer(int arg0);

	boolean glIsEnabled(int arg0);

	boolean glIsFramebuffer(int arg0);

	boolean glIsProgram(int arg0);

	boolean glIsRenderbuffer(int arg0);

	boolean glIsShader(int arg0);

	boolean glIsTexture(int arg0);

	void glLineWidth(float arg0);

	void glLinkProgram(int arg0);

	void glPixelStorei(int arg0, int arg1);

	void glPolygonOffset(float arg0, float arg1);

	void nglReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6);

	void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.ByteBuffer arg6);

	void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, float[] arg6);

	void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, short[] arg6);

	void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int[] arg6);

	void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.FloatBuffer arg6);

	void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.IntBuffer arg6);

	void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.ShortBuffer arg6);

	void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6);

	void glReleaseShaderCompiler();

	void glRenderbufferStorage(int arg0, int arg1, int arg2, int arg3);

	void glSampleCoverage(float arg0, boolean arg1);

	void glScissor(int arg0, int arg1, int arg2, int arg3);

	void nglShaderBinary(int arg0, long arg1, int arg2, long arg3, int arg4);

	void glShaderBinary(int[] arg0, int arg1, java.nio.ByteBuffer arg2);

	void glShaderBinary(java.nio.IntBuffer arg0, int arg1, java.nio.ByteBuffer arg2);

	void nglShaderSource(int arg0, int arg1, long arg2, long arg3);

	void glShaderSource(int arg0, java.lang.CharSequence arg1);

	void glShaderSource(int arg0, java.lang.CharSequence... arg1);

	void glShaderSource(int arg0, org.lwjgl.PointerBuffer arg1, int[] arg2);

	void glShaderSource(int arg0, org.lwjgl.PointerBuffer arg1, java.nio.IntBuffer arg2);

	void glStencilFunc(int arg0, int arg1, int arg2);

	void glStencilFuncSeparate(int arg0, int arg1, int arg2, int arg3);

	void glStencilMask(int arg0);

	void glStencilMaskSeparate(int arg0, int arg1);

	void glStencilOp(int arg0, int arg1, int arg2);

	void glStencilOpSeparate(int arg0, int arg1, int arg2, int arg3);

	void nglTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.ShortBuffer arg8);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.IntBuffer arg8);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.FloatBuffer arg8);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, short[] arg8);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.ByteBuffer arg8);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int[] arg8);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, float[] arg8);

	void glTexParameterf(int arg0, int arg1, float arg2);

	void nglTexParameterfv(int arg0, int arg1, long arg2);

	void glTexParameterfv(int arg0, int arg1, float[] arg2);

	void glTexParameterfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glTexParameteri(int arg0, int arg1, int arg2);

	void nglTexParameteriv(int arg0, int arg1, long arg2);

	void glTexParameteriv(int arg0, int arg1, int[] arg2);

	void glTexParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void nglTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.ByteBuffer arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, float[] arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int[] arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, short[] arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.ShortBuffer arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.IntBuffer arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.FloatBuffer arg8);

	void glUniform1f(int arg0, float arg1);

	void nglUniform1fv(int arg0, int arg1, long arg2);

	void glUniform1fv(int arg0, float[] arg1);

	void glUniform1fv(int arg0, java.nio.FloatBuffer arg1);

	void glUniform1i(int arg0, int arg1);

	void nglUniform1iv(int arg0, int arg1, long arg2);

	void glUniform1iv(int arg0, java.nio.IntBuffer arg1);

	void glUniform1iv(int arg0, int[] arg1);

	void glUniform2f(int arg0, float arg1, float arg2);

	void nglUniform2fv(int arg0, int arg1, long arg2);

	void glUniform2fv(int arg0, java.nio.FloatBuffer arg1);

	void glUniform2fv(int arg0, float[] arg1);

	void glUniform2i(int arg0, int arg1, int arg2);

	void nglUniform2iv(int arg0, int arg1, long arg2);

	void glUniform2iv(int arg0, java.nio.IntBuffer arg1);

	void glUniform2iv(int arg0, int[] arg1);

	void glUniform3f(int arg0, float arg1, float arg2, float arg3);

	void nglUniform3fv(int arg0, int arg1, long arg2);

	void glUniform3fv(int arg0, java.nio.FloatBuffer arg1);

	void glUniform3fv(int arg0, float[] arg1);

	void glUniform3i(int arg0, int arg1, int arg2, int arg3);

	void nglUniform3iv(int arg0, int arg1, long arg2);

	void glUniform3iv(int arg0, java.nio.IntBuffer arg1);

	void glUniform3iv(int arg0, int[] arg1);

	void glUniform4f(int arg0, float arg1, float arg2, float arg3, float arg4);

	void nglUniform4fv(int arg0, int arg1, long arg2);

	void glUniform4fv(int arg0, java.nio.FloatBuffer arg1);

	void glUniform4fv(int arg0, float[] arg1);

	void glUniform4i(int arg0, int arg1, int arg2, int arg3, int arg4);

	void nglUniform4iv(int arg0, int arg1, long arg2);

	void glUniform4iv(int arg0, java.nio.IntBuffer arg1);

	void glUniform4iv(int arg0, int[] arg1);

	void nglUniformMatrix2fv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix2fv(int arg0, boolean arg1, float[] arg2);

	void glUniformMatrix2fv(int arg0, boolean arg1, java.nio.FloatBuffer arg2);

	void nglUniformMatrix3fv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix3fv(int arg0, boolean arg1, java.nio.FloatBuffer arg2);

	void glUniformMatrix3fv(int arg0, boolean arg1, float[] arg2);

	void nglUniformMatrix4fv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix4fv(int arg0, boolean arg1, java.nio.FloatBuffer arg2);

	void glUniformMatrix4fv(int arg0, boolean arg1, float[] arg2);

	void glUseProgram(int arg0);

	void glValidateProgram(int arg0);

	void glVertexAttrib1f(int arg0, float arg1);

	void nglVertexAttrib1fv(int arg0, long arg1);

	void glVertexAttrib1fv(int arg0, float[] arg1);

	void glVertexAttrib1fv(int arg0, java.nio.FloatBuffer arg1);

	void glVertexAttrib2f(int arg0, float arg1, float arg2);

	void nglVertexAttrib2fv(int arg0, long arg1);

	void glVertexAttrib2fv(int arg0, float[] arg1);

	void glVertexAttrib2fv(int arg0, java.nio.FloatBuffer arg1);

	void glVertexAttrib3f(int arg0, float arg1, float arg2, float arg3);

	void nglVertexAttrib3fv(int arg0, long arg1);

	void glVertexAttrib3fv(int arg0, float[] arg1);

	void glVertexAttrib3fv(int arg0, java.nio.FloatBuffer arg1);

	void glVertexAttrib4f(int arg0, float arg1, float arg2, float arg3, float arg4);

	void nglVertexAttrib4fv(int arg0, long arg1);

	void glVertexAttrib4fv(int arg0, java.nio.FloatBuffer arg1);

	void glVertexAttrib4fv(int arg0, float[] arg1);

	void nglVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, long arg5);

	void glVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, long arg5);

	void glVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, java.nio.FloatBuffer arg5);

	void glVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, java.nio.IntBuffer arg5);

	void glVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, java.nio.ShortBuffer arg5);

	void glVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, java.nio.ByteBuffer arg5);

	void glViewport(int arg0, int arg1, int arg2, int arg3);

	void glBlendEquationi(int arg0, int arg1);

	void glBlendEquationSeparatei(int arg0, int arg1, int arg2);

	void glBlendFunci(int arg0, int arg1, int arg2);

	void glBlendFuncSeparatei(int arg0, int arg1, int arg2, int arg3, int arg4);

	void nglDrawArraysIndirect(int arg0, long arg1);

	void glDrawArraysIndirect(int arg0, long arg1);

	void glDrawArraysIndirect(int arg0, java.nio.IntBuffer arg1);

	void glDrawArraysIndirect(int arg0, int[] arg1);

	void glDrawArraysIndirect(int arg0, java.nio.ByteBuffer arg1);

	void nglDrawElementsIndirect(int arg0, int arg1, long arg2);

	void glDrawElementsIndirect(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glDrawElementsIndirect(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glDrawElementsIndirect(int arg0, int arg1, int[] arg2);

	void glDrawElementsIndirect(int arg0, int arg1, long arg2);

	void glUniform1d(int arg0, double arg1);

	void glUniform2d(int arg0, double arg1, double arg2);

	void glUniform3d(int arg0, double arg1, double arg2, double arg3);

	void glUniform4d(int arg0, double arg1, double arg2, double arg3, double arg4);

	void nglUniform1dv(int arg0, int arg1, long arg2);

	void glUniform1dv(int arg0, java.nio.DoubleBuffer arg1);

	void glUniform1dv(int arg0, double[] arg1);

	void nglUniform2dv(int arg0, int arg1, long arg2);

	void glUniform2dv(int arg0, double[] arg1);

	void glUniform2dv(int arg0, java.nio.DoubleBuffer arg1);

	void nglUniform3dv(int arg0, int arg1, long arg2);

	void glUniform3dv(int arg0, java.nio.DoubleBuffer arg1);

	void glUniform3dv(int arg0, double[] arg1);

	void nglUniform4dv(int arg0, int arg1, long arg2);

	void glUniform4dv(int arg0, double[] arg1);

	void glUniform4dv(int arg0, java.nio.DoubleBuffer arg1);

	void nglUniformMatrix2dv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix2dv(int arg0, boolean arg1, java.nio.DoubleBuffer arg2);

	void glUniformMatrix2dv(int arg0, boolean arg1, double[] arg2);

	void nglUniformMatrix3dv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix3dv(int arg0, boolean arg1, double[] arg2);

	void glUniformMatrix3dv(int arg0, boolean arg1, java.nio.DoubleBuffer arg2);

	void nglUniformMatrix4dv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix4dv(int arg0, boolean arg1, double[] arg2);

	void glUniformMatrix4dv(int arg0, boolean arg1, java.nio.DoubleBuffer arg2);

	void nglUniformMatrix2x3dv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix2x3dv(int arg0, boolean arg1, java.nio.DoubleBuffer arg2);

	void glUniformMatrix2x3dv(int arg0, boolean arg1, double[] arg2);

	void nglUniformMatrix2x4dv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix2x4dv(int arg0, boolean arg1, double[] arg2);

	void glUniformMatrix2x4dv(int arg0, boolean arg1, java.nio.DoubleBuffer arg2);

	void nglUniformMatrix3x2dv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix3x2dv(int arg0, boolean arg1, java.nio.DoubleBuffer arg2);

	void glUniformMatrix3x2dv(int arg0, boolean arg1, double[] arg2);

	void nglUniformMatrix3x4dv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix3x4dv(int arg0, boolean arg1, double[] arg2);

	void glUniformMatrix3x4dv(int arg0, boolean arg1, java.nio.DoubleBuffer arg2);

	void nglUniformMatrix4x2dv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix4x2dv(int arg0, boolean arg1, java.nio.DoubleBuffer arg2);

	void glUniformMatrix4x2dv(int arg0, boolean arg1, double[] arg2);

	void nglUniformMatrix4x3dv(int arg0, int arg1, boolean arg2, long arg3);

	void glUniformMatrix4x3dv(int arg0, boolean arg1, java.nio.DoubleBuffer arg2);

	void glUniformMatrix4x3dv(int arg0, boolean arg1, double[] arg2);

	void nglGetUniformdv(int arg0, int arg1, long arg2);

	void glGetUniformdv(int arg0, int arg1, double[] arg2);

	void glGetUniformdv(int arg0, int arg1, java.nio.DoubleBuffer arg2);

	double glGetUniformd(int arg0, int arg1);

	void glMinSampleShading(float arg0);

	int nglGetSubroutineUniformLocation(int arg0, int arg1, long arg2);

	int glGetSubroutineUniformLocation(int arg0, int arg1, java.nio.ByteBuffer arg2);

	int glGetSubroutineUniformLocation(int arg0, int arg1, java.lang.CharSequence arg2);

	int nglGetSubroutineIndex(int arg0, int arg1, long arg2);

	int glGetSubroutineIndex(int arg0, int arg1, java.nio.ByteBuffer arg2);

	int glGetSubroutineIndex(int arg0, int arg1, java.lang.CharSequence arg2);

	void nglGetActiveSubroutineUniformiv(int arg0, int arg1, int arg2, int arg3, long arg4);

	void glGetActiveSubroutineUniformiv(int arg0, int arg1, int arg2, int arg3, java.nio.IntBuffer arg4);

	void glGetActiveSubroutineUniformiv(int arg0, int arg1, int arg2, int arg3, int[] arg4);

	int glGetActiveSubroutineUniformi(int arg0, int arg1, int arg2, int arg3);

	void nglGetActiveSubroutineUniformName(int arg0, int arg1, int arg2, int arg3, long arg4, long arg5);

	void glGetActiveSubroutineUniformName(int arg0, int arg1, int arg2, int[] arg3, java.nio.ByteBuffer arg4);

	java.lang.String glGetActiveSubroutineUniformName(int arg0, int arg1, int arg2, int arg3);

	void glGetActiveSubroutineUniformName(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, java.nio.ByteBuffer arg4);

	java.lang.String glGetActiveSubroutineUniformName(int arg0, int arg1, int arg2);

	void nglGetActiveSubroutineName(int arg0, int arg1, int arg2, int arg3, long arg4, long arg5);

	void glGetActiveSubroutineName(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, java.nio.ByteBuffer arg4);

	java.lang.String glGetActiveSubroutineName(int arg0, int arg1, int arg2, int arg3);

	java.lang.String glGetActiveSubroutineName(int arg0, int arg1, int arg2);

	void glGetActiveSubroutineName(int arg0, int arg1, int arg2, int[] arg3, java.nio.ByteBuffer arg4);

	void nglUniformSubroutinesuiv(int arg0, int arg1, long arg2);

	void glUniformSubroutinesuiv(int arg0, int[] arg1);

	void glUniformSubroutinesuiv(int arg0, java.nio.IntBuffer arg1);

	void glUniformSubroutinesui(int arg0, int arg1);

	void nglGetUniformSubroutineuiv(int arg0, int arg1, long arg2);

	void glGetUniformSubroutineuiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetUniformSubroutineuiv(int arg0, int arg1, int[] arg2);

	int glGetUniformSubroutineui(int arg0, int arg1);

	void nglGetProgramStageiv(int arg0, int arg1, int arg2, long arg3);

	void glGetProgramStageiv(int arg0, int arg1, int arg2, int[] arg3);

	void glGetProgramStageiv(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	int glGetProgramStagei(int arg0, int arg1, int arg2);

	void glPatchParameteri(int arg0, int arg1);

	void nglPatchParameterfv(int arg0, long arg1);

	void glPatchParameterfv(int arg0, float[] arg1);

	void glPatchParameterfv(int arg0, java.nio.FloatBuffer arg1);

	void glDrawTransformFeedback(int arg0, int arg1);

	void glDrawTransformFeedbackStream(int arg0, int arg1, int arg2);

	void glBeginQueryIndexed(int arg0, int arg1, int arg2);

	void glEndQueryIndexed(int arg0, int arg1);

	void nglGetQueryIndexediv(int arg0, int arg1, int arg2, long arg3);

	void glGetQueryIndexediv(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	void glGetQueryIndexediv(int arg0, int arg1, int arg2, int[] arg3);

	int glGetQueryIndexedi(int arg0, int arg1, int arg2);

	void nglBindFragDataLocationIndexed(int arg0, int arg1, int arg2, long arg3);

	void glBindFragDataLocationIndexed(int arg0, int arg1, int arg2, java.nio.ByteBuffer arg3);

	void glBindFragDataLocationIndexed(int arg0, int arg1, int arg2, java.lang.CharSequence arg3);

	int nglGetFragDataIndex(int arg0, long arg1);

	int glGetFragDataIndex(int arg0, java.nio.ByteBuffer arg1);

	int glGetFragDataIndex(int arg0, java.lang.CharSequence arg1);

	void nglSamplerParameterIiv(int arg0, int arg1, long arg2);

	void glSamplerParameterIiv(int arg0, int arg1, int[] arg2);

	void glSamplerParameterIiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void nglSamplerParameterIuiv(int arg0, int arg1, long arg2);

	void glSamplerParameterIuiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glSamplerParameterIuiv(int arg0, int arg1, int[] arg2);

	void nglGetSamplerParameterIiv(int arg0, int arg1, long arg2);

	void glGetSamplerParameterIiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetSamplerParameterIiv(int arg0, int arg1, int[] arg2);

	int glGetSamplerParameterIi(int arg0, int arg1);

	void nglGetSamplerParameterIuiv(int arg0, int arg1, long arg2);

	void glGetSamplerParameterIuiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetSamplerParameterIuiv(int arg0, int arg1, int[] arg2);

	int glGetSamplerParameterIui(int arg0, int arg1);

	void glQueryCounter(int arg0, int arg1);

	void nglGetQueryObjecti64v(int arg0, int arg1, long arg2);

	void glGetQueryObjecti64v(int arg0, int arg1, java.nio.LongBuffer arg2);

	void glGetQueryObjecti64v(int arg0, int arg1, long[] arg2);

	void glGetQueryObjecti64v(int arg0, int arg1, long arg2);

	long glGetQueryObjecti64(int arg0, int arg1);

	void nglGetQueryObjectui64v(int arg0, int arg1, long arg2);

	void glGetQueryObjectui64v(int arg0, int arg1, long[] arg2);

	void glGetQueryObjectui64v(int arg0, int arg1, java.nio.LongBuffer arg2);

	void glGetQueryObjectui64v(int arg0, int arg1, long arg2);

	long glGetQueryObjectui64(int arg0, int arg1);

	void glVertexP2ui(int arg0, int arg1);

	void glVertexP3ui(int arg0, int arg1);

	void glVertexP4ui(int arg0, int arg1);

	void nglVertexP2uiv(int arg0, long arg1);

	void glVertexP2uiv(int arg0, java.nio.IntBuffer arg1);

	void glVertexP2uiv(int arg0, int[] arg1);

	void nglVertexP3uiv(int arg0, long arg1);

	void glVertexP3uiv(int arg0, java.nio.IntBuffer arg1);

	void glVertexP3uiv(int arg0, int[] arg1);

	void nglVertexP4uiv(int arg0, long arg1);

	void glVertexP4uiv(int arg0, java.nio.IntBuffer arg1);

	void glVertexP4uiv(int arg0, int[] arg1);

	void glTexCoordP1ui(int arg0, int arg1);

	void glTexCoordP2ui(int arg0, int arg1);

	void glTexCoordP3ui(int arg0, int arg1);

	void glTexCoordP4ui(int arg0, int arg1);

	void nglTexCoordP1uiv(int arg0, long arg1);

	void glTexCoordP1uiv(int arg0, int[] arg1);

	void glTexCoordP1uiv(int arg0, java.nio.IntBuffer arg1);

	void nglTexCoordP2uiv(int arg0, long arg1);

	void glTexCoordP2uiv(int arg0, int[] arg1);

	void glTexCoordP2uiv(int arg0, java.nio.IntBuffer arg1);

	void nglTexCoordP3uiv(int arg0, long arg1);

	void glTexCoordP3uiv(int arg0, int[] arg1);

	void glTexCoordP3uiv(int arg0, java.nio.IntBuffer arg1);

	void nglTexCoordP4uiv(int arg0, long arg1);

	void glTexCoordP4uiv(int arg0, java.nio.IntBuffer arg1);

	void glTexCoordP4uiv(int arg0, int[] arg1);

	void glMultiTexCoordP1ui(int arg0, int arg1, int arg2);

	void glMultiTexCoordP2ui(int arg0, int arg1, int arg2);

	void glMultiTexCoordP3ui(int arg0, int arg1, int arg2);

	void glMultiTexCoordP4ui(int arg0, int arg1, int arg2);

	void nglMultiTexCoordP1uiv(int arg0, int arg1, long arg2);

	void glMultiTexCoordP1uiv(int arg0, int arg1, int[] arg2);

	void glMultiTexCoordP1uiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void nglMultiTexCoordP2uiv(int arg0, int arg1, long arg2);

	void glMultiTexCoordP2uiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glMultiTexCoordP2uiv(int arg0, int arg1, int[] arg2);

	void nglMultiTexCoordP3uiv(int arg0, int arg1, long arg2);

	void glMultiTexCoordP3uiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glMultiTexCoordP3uiv(int arg0, int arg1, int[] arg2);

	void nglMultiTexCoordP4uiv(int arg0, int arg1, long arg2);

	void glMultiTexCoordP4uiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glMultiTexCoordP4uiv(int arg0, int arg1, int[] arg2);

	void glNormalP3ui(int arg0, int arg1);

	void nglNormalP3uiv(int arg0, long arg1);

	void glNormalP3uiv(int arg0, java.nio.IntBuffer arg1);

	void glNormalP3uiv(int arg0, int[] arg1);

	void glColorP3ui(int arg0, int arg1);

	void glColorP4ui(int arg0, int arg1);

	void nglColorP3uiv(int arg0, long arg1);

	void glColorP3uiv(int arg0, java.nio.IntBuffer arg1);

	void glColorP3uiv(int arg0, int[] arg1);

	void nglColorP4uiv(int arg0, long arg1);

	void glColorP4uiv(int arg0, int[] arg1);

	void glColorP4uiv(int arg0, java.nio.IntBuffer arg1);

	void glSecondaryColorP3ui(int arg0, int arg1);

	void nglSecondaryColorP3uiv(int arg0, long arg1);

	void glSecondaryColorP3uiv(int arg0, int[] arg1);

	void glSecondaryColorP3uiv(int arg0, java.nio.IntBuffer arg1);

	void glVertexAttribP1ui(int arg0, int arg1, boolean arg2, int arg3);

	void glVertexAttribP2ui(int arg0, int arg1, boolean arg2, int arg3);

	void glVertexAttribP3ui(int arg0, int arg1, boolean arg2, int arg3);

	void glVertexAttribP4ui(int arg0, int arg1, boolean arg2, int arg3);

	void nglVertexAttribP1uiv(int arg0, int arg1, boolean arg2, long arg3);

	void glVertexAttribP1uiv(int arg0, int arg1, boolean arg2, int[] arg3);

	void glVertexAttribP1uiv(int arg0, int arg1, boolean arg2, java.nio.IntBuffer arg3);

	void nglVertexAttribP2uiv(int arg0, int arg1, boolean arg2, long arg3);

	void glVertexAttribP2uiv(int arg0, int arg1, boolean arg2, int[] arg3);

	void glVertexAttribP2uiv(int arg0, int arg1, boolean arg2, java.nio.IntBuffer arg3);

	void nglVertexAttribP3uiv(int arg0, int arg1, boolean arg2, long arg3);

	void glVertexAttribP3uiv(int arg0, int arg1, boolean arg2, java.nio.IntBuffer arg3);

	void glVertexAttribP3uiv(int arg0, int arg1, boolean arg2, int[] arg3);

	void nglVertexAttribP4uiv(int arg0, int arg1, boolean arg2, long arg3);

	void glVertexAttribP4uiv(int arg0, int arg1, boolean arg2, java.nio.IntBuffer arg3);

	void glVertexAttribP4uiv(int arg0, int arg1, boolean arg2, int[] arg3);

	void nglDrawElementsBaseVertex(int arg0, int arg1, int arg2, long arg3, int arg4);

	void glDrawElementsBaseVertex(int arg0, java.nio.ShortBuffer arg1, int arg2);

	void glDrawElementsBaseVertex(int arg0, java.nio.IntBuffer arg1, int arg2);

	void glDrawElementsBaseVertex(int arg0, java.nio.ByteBuffer arg1, int arg2);

	void glDrawElementsBaseVertex(int arg0, int arg1, java.nio.ByteBuffer arg2, int arg3);

	void glDrawElementsBaseVertex(int arg0, int arg1, int arg2, long arg3, int arg4);

	void nglDrawRangeElementsBaseVertex(int arg0, int arg1, int arg2, int arg3, int arg4, long arg5, int arg6);

	void glDrawRangeElementsBaseVertex(int arg0, int arg1, int arg2, java.nio.ByteBuffer arg3, int arg4);

	void glDrawRangeElementsBaseVertex(int arg0, int arg1, int arg2, int arg3, java.nio.ByteBuffer arg4, int arg5);

	void glDrawRangeElementsBaseVertex(int arg0, int arg1, int arg2, int arg3, int arg4, long arg5, int arg6);

	void glDrawRangeElementsBaseVertex(int arg0, int arg1, int arg2, java.nio.ShortBuffer arg3, int arg4);

	void glDrawRangeElementsBaseVertex(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, int arg4);

	void nglDrawElementsInstancedBaseVertex(int arg0, int arg1, int arg2, long arg3, int arg4, int arg5);

	void glDrawElementsInstancedBaseVertex(int arg0, java.nio.ByteBuffer arg1, int arg2, int arg3);

	void glDrawElementsInstancedBaseVertex(int arg0, java.nio.IntBuffer arg1, int arg2, int arg3);

	void glDrawElementsInstancedBaseVertex(int arg0, int arg1, java.nio.ByteBuffer arg2, int arg3, int arg4);

	void glDrawElementsInstancedBaseVertex(int arg0, int arg1, int arg2, long arg3, int arg4, int arg5);

	void glDrawElementsInstancedBaseVertex(int arg0, java.nio.ShortBuffer arg1, int arg2, int arg3);

	void nglMultiDrawElementsBaseVertex(int arg0, long arg1, int arg2, long arg3, int arg4, long arg5);

	void glMultiDrawElementsBaseVertex(int arg0, int[] arg1, int arg2, org.lwjgl.PointerBuffer arg3, int[] arg4);

	void glMultiDrawElementsBaseVertex(int arg0, java.nio.IntBuffer arg1, int arg2, org.lwjgl.PointerBuffer arg3, java.nio.IntBuffer arg4);

	void glProvokingVertex(int arg0);

	void glTexImage2DMultisample(int arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5);

	void glTexImage3DMultisample(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, boolean arg6);

	void nglGetMultisamplefv(int arg0, int arg1, long arg2);

	void glGetMultisamplefv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glGetMultisamplefv(int arg0, int arg1, float[] arg2);

	float glGetMultisamplef(int arg0, int arg1);

	void glSampleMaski(int arg0, int arg1);

	void glFramebufferTexture(int arg0, int arg1, int arg2, int arg3);

	int glGetUniformIndices(int arg0, java.lang.CharSequence arg1);

	void glGetUniformIndices(int arg0, java.lang.CharSequence[] arg1, java.nio.IntBuffer arg2);

	void glPrimitiveRestartIndex(int arg0);

	void glTexBuffer(int arg0, int arg1, int arg2);

	int glGetActiveUniformsi(int arg0, int arg1, int arg2);

	void nglGetActiveUniformName(int arg0, int arg1, int arg2, long arg3, long arg4);

	void glGetActiveUniformName(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.ByteBuffer arg3);

	void glGetActiveUniformName(int arg0, int arg1, int[] arg2, java.nio.ByteBuffer arg3);

	java.lang.String glGetActiveUniformName(int arg0, int arg1);

	java.lang.String glGetActiveUniformName(int arg0, int arg1, int arg2);

	void glVertexAttribI1i(int arg0, int arg1);

	void glVertexAttribI2i(int arg0, int arg1, int arg2);

	void glVertexAttribI3i(int arg0, int arg1, int arg2, int arg3);

	void glVertexAttribI1ui(int arg0, int arg1);

	void glVertexAttribI2ui(int arg0, int arg1, int arg2);

	void glVertexAttribI3ui(int arg0, int arg1, int arg2, int arg3);

	void nglVertexAttribI1iv(int arg0, long arg1);

	void glVertexAttribI1iv(int arg0, java.nio.IntBuffer arg1);

	void glVertexAttribI1iv(int arg0, int[] arg1);

	void nglVertexAttribI2iv(int arg0, long arg1);

	void glVertexAttribI2iv(int arg0, java.nio.IntBuffer arg1);

	void glVertexAttribI2iv(int arg0, int[] arg1);

	void nglVertexAttribI3iv(int arg0, long arg1);

	void glVertexAttribI3iv(int arg0, int[] arg1);

	void glVertexAttribI3iv(int arg0, java.nio.IntBuffer arg1);

	void nglVertexAttribI1uiv(int arg0, long arg1);

	void glVertexAttribI1uiv(int arg0, int[] arg1);

	void glVertexAttribI1uiv(int arg0, java.nio.IntBuffer arg1);

	void nglVertexAttribI2uiv(int arg0, long arg1);

	void glVertexAttribI2uiv(int arg0, int[] arg1);

	void glVertexAttribI2uiv(int arg0, java.nio.IntBuffer arg1);

	void nglVertexAttribI3uiv(int arg0, long arg1);

	void glVertexAttribI3uiv(int arg0, java.nio.IntBuffer arg1);

	void glVertexAttribI3uiv(int arg0, int[] arg1);

	void nglVertexAttribI4bv(int arg0, long arg1);

	void glVertexAttribI4bv(int arg0, java.nio.ByteBuffer arg1);

	void nglVertexAttribI4sv(int arg0, long arg1);

	void glVertexAttribI4sv(int arg0, java.nio.ShortBuffer arg1);

	void glVertexAttribI4sv(int arg0, short[] arg1);

	void nglVertexAttribI4ubv(int arg0, long arg1);

	void glVertexAttribI4ubv(int arg0, java.nio.ByteBuffer arg1);

	void nglVertexAttribI4usv(int arg0, long arg1);

	void glVertexAttribI4usv(int arg0, short[] arg1);

	void glVertexAttribI4usv(int arg0, java.nio.ShortBuffer arg1);

	void nglBindFragDataLocation(int arg0, int arg1, long arg2);

	void glBindFragDataLocation(int arg0, int arg1, java.lang.CharSequence arg2);

	void glBindFragDataLocation(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glBeginConditionalRender(int arg0, int arg1);

	void glEndConditionalRender();

	void glClampColor(int arg0, int arg1);

	void glFramebufferTexture1D(int arg0, int arg1, int arg2, int arg3, int arg4);

	void glFramebufferTexture3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5);

	void nglTexParameterIiv(int arg0, int arg1, long arg2);

	void glTexParameterIiv(int arg0, int arg1, int[] arg2);

	void glTexParameterIiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glTexParameterIi(int arg0, int arg1, int arg2);

	void nglTexParameterIuiv(int arg0, int arg1, long arg2);

	void glTexParameterIuiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glTexParameterIuiv(int arg0, int arg1, int[] arg2);

	void glTexParameterIui(int arg0, int arg1, int arg2);

	void nglGetTexParameterIiv(int arg0, int arg1, long arg2);

	void glGetTexParameterIiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetTexParameterIiv(int arg0, int arg1, int[] arg2);

	int glGetTexParameterIi(int arg0, int arg1);

	void nglGetTexParameterIuiv(int arg0, int arg1, long arg2);

	void glGetTexParameterIuiv(int arg0, int arg1, int[] arg2);

	void glGetTexParameterIuiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	int glGetTexParameterIui(int arg0, int arg1);

	void glColorMaski(int arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4);

	void nglGetBooleani_v(int arg0, int arg1, long arg2);

	void glGetBooleani_v(int arg0, int arg1, java.nio.ByteBuffer arg2);

	boolean glGetBooleani(int arg0, int arg1);

	void glEnablei(int arg0, int arg1);

	void glDisablei(int arg0, int arg1);

	boolean glIsEnabledi(int arg0, int arg1);

	void glVertexAttrib1s(int arg0, short arg1);

	void glVertexAttrib1d(int arg0, double arg1);

	void glVertexAttrib2s(int arg0, short arg1, short arg2);

	void glVertexAttrib2d(int arg0, double arg1, double arg2);

	void glVertexAttrib3s(int arg0, short arg1, short arg2, short arg3);

	void glVertexAttrib3d(int arg0, double arg1, double arg2, double arg3);

	void glVertexAttrib4s(int arg0, short arg1, short arg2, short arg3, short arg4);

	void glVertexAttrib4d(int arg0, double arg1, double arg2, double arg3, double arg4);

	void glVertexAttrib4Nub(int arg0, byte arg1, byte arg2, byte arg3, byte arg4);

	void nglVertexAttrib1sv(int arg0, long arg1);

	void glVertexAttrib1sv(int arg0, java.nio.ShortBuffer arg1);

	void glVertexAttrib1sv(int arg0, short[] arg1);

	void nglVertexAttrib1dv(int arg0, long arg1);

	void glVertexAttrib1dv(int arg0, double[] arg1);

	void glVertexAttrib1dv(int arg0, java.nio.DoubleBuffer arg1);

	void nglVertexAttrib2sv(int arg0, long arg1);

	void glVertexAttrib2sv(int arg0, short[] arg1);

	void glVertexAttrib2sv(int arg0, java.nio.ShortBuffer arg1);

	void nglVertexAttrib2dv(int arg0, long arg1);

	void glVertexAttrib2dv(int arg0, double[] arg1);

	void glVertexAttrib2dv(int arg0, java.nio.DoubleBuffer arg1);

	void nglVertexAttrib3sv(int arg0, long arg1);

	void glVertexAttrib3sv(int arg0, short[] arg1);

	void glVertexAttrib3sv(int arg0, java.nio.ShortBuffer arg1);

	void nglVertexAttrib3dv(int arg0, long arg1);

	void glVertexAttrib3dv(int arg0, double[] arg1);

	void glVertexAttrib3dv(int arg0, java.nio.DoubleBuffer arg1);

	void nglVertexAttrib4sv(int arg0, long arg1);

	void glVertexAttrib4sv(int arg0, short[] arg1);

	void glVertexAttrib4sv(int arg0, java.nio.ShortBuffer arg1);

	void nglVertexAttrib4dv(int arg0, long arg1);

	void glVertexAttrib4dv(int arg0, java.nio.DoubleBuffer arg1);

	void glVertexAttrib4dv(int arg0, double[] arg1);

	void nglVertexAttrib4iv(int arg0, long arg1);

	void glVertexAttrib4iv(int arg0, java.nio.IntBuffer arg1);

	void glVertexAttrib4iv(int arg0, int[] arg1);

	void nglVertexAttrib4bv(int arg0, long arg1);

	void glVertexAttrib4bv(int arg0, java.nio.ByteBuffer arg1);

	void nglVertexAttrib4ubv(int arg0, long arg1);

	void glVertexAttrib4ubv(int arg0, java.nio.ByteBuffer arg1);

	void nglVertexAttrib4usv(int arg0, long arg1);

	void glVertexAttrib4usv(int arg0, short[] arg1);

	void glVertexAttrib4usv(int arg0, java.nio.ShortBuffer arg1);

	void nglVertexAttrib4uiv(int arg0, long arg1);

	void glVertexAttrib4uiv(int arg0, int[] arg1);

	void glVertexAttrib4uiv(int arg0, java.nio.IntBuffer arg1);

	void nglVertexAttrib4Nbv(int arg0, long arg1);

	void glVertexAttrib4Nbv(int arg0, java.nio.ByteBuffer arg1);

	void nglVertexAttrib4Nsv(int arg0, long arg1);

	void glVertexAttrib4Nsv(int arg0, short[] arg1);

	void glVertexAttrib4Nsv(int arg0, java.nio.ShortBuffer arg1);

	void nglVertexAttrib4Niv(int arg0, long arg1);

	void glVertexAttrib4Niv(int arg0, int[] arg1);

	void glVertexAttrib4Niv(int arg0, java.nio.IntBuffer arg1);

	void nglVertexAttrib4Nubv(int arg0, long arg1);

	void glVertexAttrib4Nubv(int arg0, java.nio.ByteBuffer arg1);

	void nglVertexAttrib4Nusv(int arg0, long arg1);

	void glVertexAttrib4Nusv(int arg0, short[] arg1);

	void glVertexAttrib4Nusv(int arg0, java.nio.ShortBuffer arg1);

	void nglVertexAttrib4Nuiv(int arg0, long arg1);

	void glVertexAttrib4Nuiv(int arg0, int[] arg1);

	void glVertexAttrib4Nuiv(int arg0, java.nio.IntBuffer arg1);

	int glGetVertexAttribi(int arg0, int arg1);

	void nglGetVertexAttribdv(int arg0, int arg1, long arg2);

	void glGetVertexAttribdv(int arg0, int arg1, double[] arg2);

	void glGetVertexAttribdv(int arg0, int arg1, java.nio.DoubleBuffer arg2);

	void glGetQueryObjectuiv(int arg0, int arg1, long arg2);

	void glBufferData(int arg0, long[] arg1, int arg2);

	void glBufferData(int arg0, java.nio.LongBuffer arg1, int arg2);

	void glBufferData(int arg0, java.nio.DoubleBuffer arg1, int arg2);

	void glBufferData(int arg0, double[] arg1, int arg2);

	void glBufferSubData(int arg0, long arg1, double[] arg2);

	void glBufferSubData(int arg0, long arg1, java.nio.DoubleBuffer arg2);

	void glBufferSubData(int arg0, long arg1, long[] arg2);

	void glBufferSubData(int arg0, long arg1, java.nio.LongBuffer arg2);

	void nglGetBufferSubData(int arg0, long arg1, long arg2, long arg3);

	void glGetBufferSubData(int arg0, long arg1, short[] arg2);

	void glGetBufferSubData(int arg0, long arg1, java.nio.ByteBuffer arg2);

	void glGetBufferSubData(int arg0, long arg1, java.nio.ShortBuffer arg2);

	void glGetBufferSubData(int arg0, long arg1, double[] arg2);

	void glGetBufferSubData(int arg0, long arg1, float[] arg2);

	void glGetBufferSubData(int arg0, long arg1, long[] arg2);

	void glGetBufferSubData(int arg0, long arg1, int[] arg2);

	void glGetBufferSubData(int arg0, long arg1, java.nio.DoubleBuffer arg2);

	void glGetBufferSubData(int arg0, long arg1, java.nio.FloatBuffer arg2);

	void glGetBufferSubData(int arg0, long arg1, java.nio.LongBuffer arg2);

	void glGetBufferSubData(int arg0, long arg1, java.nio.IntBuffer arg2);

	long nglMapBuffer(int arg0, int arg1);

	java.nio.ByteBuffer glMapBuffer(int arg0, int arg1, long arg2, java.nio.ByteBuffer arg3);

	java.nio.ByteBuffer glMapBuffer(int arg0, int arg1, java.nio.ByteBuffer arg2);

	java.nio.ByteBuffer glMapBuffer(int arg0, int arg1);

	void nglGetQueryObjectiv(int arg0, int arg1, long arg2);

	void glGetQueryObjectiv(int arg0, int arg1, int[] arg2);

	void glGetQueryObjectiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetQueryObjectiv(int arg0, int arg1, long arg2);

	int glGetQueryObjecti(int arg0, int arg1);

	void glFogCoordf(float arg0);

	void glFogCoordd(double arg0);

	void nglFogCoordfv(long arg0);

	void glFogCoordfv(float[] arg0);

	void glFogCoordfv(java.nio.FloatBuffer arg0);

	void nglFogCoorddv(long arg0);

	void glFogCoorddv(double[] arg0);

	void glFogCoorddv(java.nio.DoubleBuffer arg0);

	void nglFogCoordPointer(int arg0, int arg1, long arg2);

	void glFogCoordPointer(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glFogCoordPointer(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glFogCoordPointer(int arg0, int arg1, long arg2);

	void glFogCoordPointer(int arg0, int arg1, java.nio.ShortBuffer arg2);

	void nglMultiDrawArrays(int arg0, long arg1, long arg2, int arg3);

	void glMultiDrawArrays(int arg0, int[] arg1, int[] arg2);

	void glMultiDrawArrays(int arg0, java.nio.IntBuffer arg1, java.nio.IntBuffer arg2);

	void nglMultiDrawElements(int arg0, long arg1, int arg2, long arg3, int arg4);

	void glMultiDrawElements(int arg0, java.nio.IntBuffer arg1, int arg2, org.lwjgl.PointerBuffer arg3);

	void glMultiDrawElements(int arg0, int[] arg1, int arg2, org.lwjgl.PointerBuffer arg3);

	void glPointParameterf(int arg0, float arg1);

	void glPointParameteri(int arg0, int arg1);

	void nglPointParameterfv(int arg0, long arg1);

	void glPointParameterfv(int arg0, java.nio.FloatBuffer arg1);

	void glPointParameterfv(int arg0, float[] arg1);

	void nglPointParameteriv(int arg0, long arg1);

	void glPointParameteriv(int arg0, java.nio.IntBuffer arg1);

	void glPointParameteriv(int arg0, int[] arg1);

	void glSecondaryColor3b(byte arg0, byte arg1, byte arg2);

	void glSecondaryColor3s(short arg0, short arg1, short arg2);

	void glSecondaryColor3i(int arg0, int arg1, int arg2);

	void glSecondaryColor3f(float arg0, float arg1, float arg2);

	void glSecondaryColor3d(double arg0, double arg1, double arg2);

	void glSecondaryColor3ub(byte arg0, byte arg1, byte arg2);

	void glSecondaryColor3us(short arg0, short arg1, short arg2);

	void glSecondaryColor3ui(int arg0, int arg1, int arg2);

	void nglSecondaryColor3bv(long arg0);

	void glSecondaryColor3bv(java.nio.ByteBuffer arg0);

	void nglSecondaryColor3sv(long arg0);

	void glSecondaryColor3sv(short[] arg0);

	void glSecondaryColor3sv(java.nio.ShortBuffer arg0);

	void nglSecondaryColor3iv(long arg0);

	void glSecondaryColor3iv(int[] arg0);

	void glSecondaryColor3iv(java.nio.IntBuffer arg0);

	void nglSecondaryColor3fv(long arg0);

	void glSecondaryColor3fv(java.nio.FloatBuffer arg0);

	void glSecondaryColor3fv(float[] arg0);

	void nglSecondaryColor3dv(long arg0);

	void glSecondaryColor3dv(double[] arg0);

	void glSecondaryColor3dv(java.nio.DoubleBuffer arg0);

	void nglSecondaryColor3ubv(long arg0);

	void glSecondaryColor3ubv(java.nio.ByteBuffer arg0);

	void nglSecondaryColor3usv(long arg0);

	void glSecondaryColor3usv(java.nio.ShortBuffer arg0);

	void glSecondaryColor3usv(short[] arg0);

	void nglSecondaryColor3uiv(long arg0);

	void glSecondaryColor3uiv(int[] arg0);

	void glSecondaryColor3uiv(java.nio.IntBuffer arg0);

	void nglSecondaryColorPointer(int arg0, int arg1, int arg2, long arg3);

	void glSecondaryColorPointer(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	void glSecondaryColorPointer(int arg0, int arg1, int arg2, java.nio.ShortBuffer arg3);

	void glSecondaryColorPointer(int arg0, int arg1, int arg2, java.nio.ByteBuffer arg3);

	void glSecondaryColorPointer(int arg0, int arg1, int arg2, java.nio.FloatBuffer arg3);

	void glSecondaryColorPointer(int arg0, int arg1, int arg2, long arg3);

	void glWindowPos2i(int arg0, int arg1);

	void glWindowPos2s(short arg0, short arg1);

	void glWindowPos2f(float arg0, float arg1);

	void glWindowPos2d(double arg0, double arg1);

	void nglWindowPos2iv(long arg0);

	void glWindowPos2iv(java.nio.IntBuffer arg0);

	void glWindowPos2iv(int[] arg0);

	void nglWindowPos2sv(long arg0);

	void glWindowPos2sv(java.nio.ShortBuffer arg0);

	void glWindowPos2sv(short[] arg0);

	void nglWindowPos2fv(long arg0);

	void glWindowPos2fv(java.nio.FloatBuffer arg0);

	void glWindowPos2fv(float[] arg0);

	void nglWindowPos2dv(long arg0);

	void glWindowPos2dv(java.nio.DoubleBuffer arg0);

	void glWindowPos2dv(double[] arg0);

	void glWindowPos3i(int arg0, int arg1, int arg2);

	void glWindowPos3s(short arg0, short arg1, short arg2);

	void glWindowPos3f(float arg0, float arg1, float arg2);

	void glWindowPos3d(double arg0, double arg1, double arg2);

	void nglWindowPos3iv(long arg0);

	void glWindowPos3iv(java.nio.IntBuffer arg0);

	void glWindowPos3iv(int[] arg0);

	void nglWindowPos3sv(long arg0);

	void glWindowPos3sv(short[] arg0);

	void glWindowPos3sv(java.nio.ShortBuffer arg0);

	void nglWindowPos3fv(long arg0);

	void glWindowPos3fv(java.nio.FloatBuffer arg0);

	void glWindowPos3fv(float[] arg0);

	void nglWindowPos3dv(long arg0);

	void glWindowPos3dv(java.nio.DoubleBuffer arg0);

	void glWindowPos3dv(double[] arg0);

	void nglCompressedTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6);

	void glCompressedTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, java.nio.ByteBuffer arg5);

	void glCompressedTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6);

	void nglCompressedTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6);

	void glCompressedTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, java.nio.ByteBuffer arg5);

	void glCompressedTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6);

	void nglGetCompressedTexImage(int arg0, int arg1, long arg2);

	void glGetCompressedTexImage(int arg0, int arg1, long arg2);

	void glGetCompressedTexImage(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glClientActiveTexture(int arg0);

	void glMultiTexCoord1f(int arg0, float arg1);

	void glMultiTexCoord1s(int arg0, short arg1);

	void glMultiTexCoord1i(int arg0, int arg1);

	void glMultiTexCoord1d(int arg0, double arg1);

	void nglMultiTexCoord1fv(int arg0, long arg1);

	void glMultiTexCoord1fv(int arg0, java.nio.FloatBuffer arg1);

	void glMultiTexCoord1fv(int arg0, float[] arg1);

	void nglMultiTexCoord1sv(int arg0, long arg1);

	void glMultiTexCoord1sv(int arg0, short[] arg1);

	void glMultiTexCoord1sv(int arg0, java.nio.ShortBuffer arg1);

	void nglMultiTexCoord1iv(int arg0, long arg1);

	void glMultiTexCoord1iv(int arg0, int[] arg1);

	void glMultiTexCoord1iv(int arg0, java.nio.IntBuffer arg1);

	void nglMultiTexCoord1dv(int arg0, long arg1);

	void glMultiTexCoord1dv(int arg0, double[] arg1);

	void glMultiTexCoord1dv(int arg0, java.nio.DoubleBuffer arg1);

	void glMultiTexCoord2f(int arg0, float arg1, float arg2);

	void glMultiTexCoord2s(int arg0, short arg1, short arg2);

	void glMultiTexCoord2i(int arg0, int arg1, int arg2);

	void glMultiTexCoord2d(int arg0, double arg1, double arg2);

	void nglMultiTexCoord2fv(int arg0, long arg1);

	void glMultiTexCoord2fv(int arg0, float[] arg1);

	void glMultiTexCoord2fv(int arg0, java.nio.FloatBuffer arg1);

	void nglMultiTexCoord2sv(int arg0, long arg1);

	void glMultiTexCoord2sv(int arg0, short[] arg1);

	void glMultiTexCoord2sv(int arg0, java.nio.ShortBuffer arg1);

	void nglMultiTexCoord2iv(int arg0, long arg1);

	void glMultiTexCoord2iv(int arg0, java.nio.IntBuffer arg1);

	void glMultiTexCoord2iv(int arg0, int[] arg1);

	void nglMultiTexCoord2dv(int arg0, long arg1);

	void glMultiTexCoord2dv(int arg0, double[] arg1);

	void glMultiTexCoord2dv(int arg0, java.nio.DoubleBuffer arg1);

	void glMultiTexCoord3f(int arg0, float arg1, float arg2, float arg3);

	void glMultiTexCoord3s(int arg0, short arg1, short arg2, short arg3);

	void glMultiTexCoord3i(int arg0, int arg1, int arg2, int arg3);

	void glMultiTexCoord3d(int arg0, double arg1, double arg2, double arg3);

	void nglMultiTexCoord3fv(int arg0, long arg1);

	void glMultiTexCoord3fv(int arg0, float[] arg1);

	void glMultiTexCoord3fv(int arg0, java.nio.FloatBuffer arg1);

	void nglMultiTexCoord3sv(int arg0, long arg1);

	void glMultiTexCoord3sv(int arg0, short[] arg1);

	void glMultiTexCoord3sv(int arg0, java.nio.ShortBuffer arg1);

	void nglMultiTexCoord3iv(int arg0, long arg1);

	void glMultiTexCoord3iv(int arg0, int[] arg1);

	void glMultiTexCoord3iv(int arg0, java.nio.IntBuffer arg1);

	void nglMultiTexCoord3dv(int arg0, long arg1);

	void glMultiTexCoord3dv(int arg0, java.nio.DoubleBuffer arg1);

	void glMultiTexCoord3dv(int arg0, double[] arg1);

	void glMultiTexCoord4f(int arg0, float arg1, float arg2, float arg3, float arg4);

	void glMultiTexCoord4s(int arg0, short arg1, short arg2, short arg3, short arg4);

	void glMultiTexCoord4i(int arg0, int arg1, int arg2, int arg3, int arg4);

	void glMultiTexCoord4d(int arg0, double arg1, double arg2, double arg3, double arg4);

	void nglMultiTexCoord4fv(int arg0, long arg1);

	void glMultiTexCoord4fv(int arg0, float[] arg1);

	void glMultiTexCoord4fv(int arg0, java.nio.FloatBuffer arg1);

	void nglMultiTexCoord4sv(int arg0, long arg1);

	void glMultiTexCoord4sv(int arg0, short[] arg1);

	void glMultiTexCoord4sv(int arg0, java.nio.ShortBuffer arg1);

	void nglMultiTexCoord4iv(int arg0, long arg1);

	void glMultiTexCoord4iv(int arg0, int[] arg1);

	void glMultiTexCoord4iv(int arg0, java.nio.IntBuffer arg1);

	void nglMultiTexCoord4dv(int arg0, long arg1);

	void glMultiTexCoord4dv(int arg0, double[] arg1);

	void glMultiTexCoord4dv(int arg0, java.nio.DoubleBuffer arg1);

	void nglLoadTransposeMatrixf(long arg0);

	void glLoadTransposeMatrixf(java.nio.FloatBuffer arg0);

	void glLoadTransposeMatrixf(float[] arg0);

	void nglLoadTransposeMatrixd(long arg0);

	void glLoadTransposeMatrixd(double[] arg0);

	void glLoadTransposeMatrixd(java.nio.DoubleBuffer arg0);

	void nglMultTransposeMatrixf(long arg0);

	void glMultTransposeMatrixf(float[] arg0);

	void glMultTransposeMatrixf(java.nio.FloatBuffer arg0);

	void nglMultTransposeMatrixd(long arg0);

	void glMultTransposeMatrixd(java.nio.DoubleBuffer arg0);

	void glMultTransposeMatrixd(double[] arg0);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, double[] arg9);

	void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, java.nio.DoubleBuffer arg9);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, java.nio.DoubleBuffer arg10);

	void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, double[] arg10);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.DoubleBuffer arg8);

	void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, double[] arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, double[] arg8);

	void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.DoubleBuffer arg8);

	void glAccum(int arg0, float arg1);

	void glAlphaFunc(int arg0, float arg1);

	boolean nglAreTexturesResident(int arg0, long arg1, long arg2);

	boolean glAreTexturesResident(int[] arg0, java.nio.ByteBuffer arg1);

	boolean glAreTexturesResident(int arg0, java.nio.ByteBuffer arg1);

	boolean glAreTexturesResident(java.nio.IntBuffer arg0, java.nio.ByteBuffer arg1);

	void glArrayElement(int arg0);

	void glBegin(int arg0);

	void nglBitmap(int arg0, int arg1, float arg2, float arg3, float arg4, float arg5, long arg6);

	void glBitmap(int arg0, int arg1, float arg2, float arg3, float arg4, float arg5, long arg6);

	void glBitmap(int arg0, int arg1, float arg2, float arg3, float arg4, float arg5, java.nio.ByteBuffer arg6);

	void glCallList(int arg0);

	void nglCallLists(int arg0, int arg1, long arg2);

	void glCallLists(int arg0, java.nio.ByteBuffer arg1);

	void glCallLists(java.nio.IntBuffer arg0);

	void glCallLists(java.nio.ByteBuffer arg0);

	void glCallLists(java.nio.ShortBuffer arg0);

	void glClearAccum(float arg0, float arg1, float arg2, float arg3);

	void glClearDepth(double arg0);

	void glClearIndex(float arg0);

	void nglClipPlane(int arg0, long arg1);

	void glClipPlane(int arg0, double[] arg1);

	void glClipPlane(int arg0, java.nio.DoubleBuffer arg1);

	void glColor3b(byte arg0, byte arg1, byte arg2);

	void glColor3s(short arg0, short arg1, short arg2);

	void glColor3i(int arg0, int arg1, int arg2);

	void glColor3f(float arg0, float arg1, float arg2);

	void glColor3d(double arg0, double arg1, double arg2);

	void glColor3ub(byte arg0, byte arg1, byte arg2);

	void glColor3us(short arg0, short arg1, short arg2);

	void glColor3ui(int arg0, int arg1, int arg2);

	void nglColor3bv(long arg0);

	void glColor3bv(java.nio.ByteBuffer arg0);

	void nglColor3sv(long arg0);

	void glColor3sv(short[] arg0);

	void glColor3sv(java.nio.ShortBuffer arg0);

	void nglColor3iv(long arg0);

	void glColor3iv(int[] arg0);

	void glColor3iv(java.nio.IntBuffer arg0);

	void nglColor3fv(long arg0);

	void glColor3fv(java.nio.FloatBuffer arg0);

	void glColor3fv(float[] arg0);

	void nglColor3dv(long arg0);

	void glColor3dv(double[] arg0);

	void glColor3dv(java.nio.DoubleBuffer arg0);

	void nglColor3ubv(long arg0);

	void glColor3ubv(java.nio.ByteBuffer arg0);

	void nglColor3usv(long arg0);

	void glColor3usv(java.nio.ShortBuffer arg0);

	void glColor3usv(short[] arg0);

	void nglColor3uiv(long arg0);

	void glColor3uiv(int[] arg0);

	void glColor3uiv(java.nio.IntBuffer arg0);

	void glColor4b(byte arg0, byte arg1, byte arg2, byte arg3);

	void glColor4s(short arg0, short arg1, short arg2, short arg3);

	void glColor4i(int arg0, int arg1, int arg2, int arg3);

	void glColor4f(float arg0, float arg1, float arg2, float arg3);

	void glColor4d(double arg0, double arg1, double arg2, double arg3);

	void glColor4ub(byte arg0, byte arg1, byte arg2, byte arg3);

	void glColor4us(short arg0, short arg1, short arg2, short arg3);

	void glColor4ui(int arg0, int arg1, int arg2, int arg3);

	void nglColor4bv(long arg0);

	void glColor4bv(java.nio.ByteBuffer arg0);

	void nglColor4sv(long arg0);

	void glColor4sv(java.nio.ShortBuffer arg0);

	void glColor4sv(short[] arg0);

	void nglColor4iv(long arg0);

	void glColor4iv(java.nio.IntBuffer arg0);

	void glColor4iv(int[] arg0);

	void nglColor4fv(long arg0);

	void glColor4fv(float[] arg0);

	void glColor4fv(java.nio.FloatBuffer arg0);

	void nglColor4dv(long arg0);

	void glColor4dv(java.nio.DoubleBuffer arg0);

	void glColor4dv(double[] arg0);

	void nglColor4ubv(long arg0);

	void glColor4ubv(java.nio.ByteBuffer arg0);

	void nglColor4usv(long arg0);

	void glColor4usv(short[] arg0);

	void glColor4usv(java.nio.ShortBuffer arg0);

	void nglColor4uiv(long arg0);

	void glColor4uiv(java.nio.IntBuffer arg0);

	void glColor4uiv(int[] arg0);

	void glColorMaterial(int arg0, int arg1);

	void nglColorPointer(int arg0, int arg1, int arg2, long arg3);

	void glColorPointer(int arg0, int arg1, int arg2, java.nio.ByteBuffer arg3);

	void glColorPointer(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	void glColorPointer(int arg0, int arg1, int arg2, java.nio.FloatBuffer arg3);

	void glColorPointer(int arg0, int arg1, int arg2, java.nio.ShortBuffer arg3);

	void glColorPointer(int arg0, int arg1, int arg2, long arg3);

	void glCopyPixels(int arg0, int arg1, int arg2, int arg3, int arg4);

	void glDeleteLists(int arg0, int arg1);

	void glDepthRange(double arg0, double arg1);

	void glDisableClientState(int arg0);

	void glDrawBuffer(int arg0);

	void nglDrawPixels(int arg0, int arg1, int arg2, int arg3, long arg4);

	void glDrawPixels(int arg0, int arg1, int arg2, int arg3, java.nio.ShortBuffer arg4);

	void glDrawPixels(int arg0, int arg1, int arg2, int arg3, float[] arg4);

	void glDrawPixels(int arg0, int arg1, int arg2, int arg3, int[] arg4);

	void glDrawPixels(int arg0, int arg1, int arg2, int arg3, short[] arg4);

	void glDrawPixels(int arg0, int arg1, int arg2, int arg3, long arg4);

	void glDrawPixels(int arg0, int arg1, int arg2, int arg3, java.nio.FloatBuffer arg4);

	void glDrawPixels(int arg0, int arg1, int arg2, int arg3, java.nio.ByteBuffer arg4);

	void glDrawPixels(int arg0, int arg1, int arg2, int arg3, java.nio.IntBuffer arg4);

	void glEdgeFlag(boolean arg0);

	void nglEdgeFlagv(long arg0);

	void glEdgeFlagv(java.nio.ByteBuffer arg0);

	void nglEdgeFlagPointer(int arg0, long arg1);

	void glEdgeFlagPointer(int arg0, java.nio.ByteBuffer arg1);

	void glEdgeFlagPointer(int arg0, long arg1);

	void glEnableClientState(int arg0);

	void glEnd();

	void glEvalCoord1f(float arg0);

	void nglEvalCoord1fv(long arg0);

	void glEvalCoord1fv(float[] arg0);

	void glEvalCoord1fv(java.nio.FloatBuffer arg0);

	void glEvalCoord1d(double arg0);

	void nglEvalCoord1dv(long arg0);

	void glEvalCoord1dv(java.nio.DoubleBuffer arg0);

	void glEvalCoord1dv(double[] arg0);

	void glEvalCoord2f(float arg0, float arg1);

	void nglEvalCoord2fv(long arg0);

	void glEvalCoord2fv(float[] arg0);

	void glEvalCoord2fv(java.nio.FloatBuffer arg0);

	void glEvalCoord2d(double arg0, double arg1);

	void nglEvalCoord2dv(long arg0);

	void glEvalCoord2dv(java.nio.DoubleBuffer arg0);

	void glEvalCoord2dv(double[] arg0);

	void glEvalMesh1(int arg0, int arg1, int arg2);

	void glEvalMesh2(int arg0, int arg1, int arg2, int arg3, int arg4);

	void glEvalPoint1(int arg0);

	void glEvalPoint2(int arg0, int arg1);

	void nglFeedbackBuffer(int arg0, int arg1, long arg2);

	void glFeedbackBuffer(int arg0, float[] arg1);

	void glFeedbackBuffer(int arg0, java.nio.FloatBuffer arg1);

	void glFogi(int arg0, int arg1);

	void nglFogiv(int arg0, long arg1);

	void glFogiv(int arg0, java.nio.IntBuffer arg1);

	void glFogiv(int arg0, int[] arg1);

	void glFogf(int arg0, float arg1);

	void nglFogfv(int arg0, long arg1);

	void glFogfv(int arg0, java.nio.FloatBuffer arg1);

	void glFogfv(int arg0, float[] arg1);

	int glGenLists(int arg0);

	void nglGetClipPlane(int arg0, long arg1);

	void glGetClipPlane(int arg0, double[] arg1);

	void glGetClipPlane(int arg0, java.nio.DoubleBuffer arg1);

	void nglGetDoublev(int arg0, long arg1);

	void glGetDoublev(int arg0, java.nio.DoubleBuffer arg1);

	void glGetDoublev(int arg0, double[] arg1);

	double glGetDouble(int arg0);

	void nglGetLightiv(int arg0, int arg1, long arg2);

	void glGetLightiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetLightiv(int arg0, int arg1, int[] arg2);

	int glGetLighti(int arg0, int arg1);

	void nglGetLightfv(int arg0, int arg1, long arg2);

	void glGetLightfv(int arg0, int arg1, float[] arg2);

	void glGetLightfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	float glGetLightf(int arg0, int arg1);

	void nglGetMapiv(int arg0, int arg1, long arg2);

	void glGetMapiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetMapiv(int arg0, int arg1, int[] arg2);

	int glGetMapi(int arg0, int arg1);

	void nglGetMapfv(int arg0, int arg1, long arg2);

	void glGetMapfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glGetMapfv(int arg0, int arg1, float[] arg2);

	float glGetMapf(int arg0, int arg1);

	void nglGetMapdv(int arg0, int arg1, long arg2);

	void glGetMapdv(int arg0, int arg1, java.nio.DoubleBuffer arg2);

	void glGetMapdv(int arg0, int arg1, double[] arg2);

	double glGetMapd(int arg0, int arg1);

	void nglGetMaterialiv(int arg0, int arg1, long arg2);

	void glGetMaterialiv(int arg0, int arg1, int[] arg2);

	void glGetMaterialiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void nglGetMaterialfv(int arg0, int arg1, long arg2);

	void glGetMaterialfv(int arg0, int arg1, float[] arg2);

	void glGetMaterialfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void nglGetPixelMapfv(int arg0, long arg1);

	void glGetPixelMapfv(int arg0, float[] arg1);

	void glGetPixelMapfv(int arg0, long arg1);

	void glGetPixelMapfv(int arg0, java.nio.FloatBuffer arg1);

	void nglGetPixelMapusv(int arg0, long arg1);

	void glGetPixelMapusv(int arg0, java.nio.ShortBuffer arg1);

	void glGetPixelMapusv(int arg0, long arg1);

	void glGetPixelMapusv(int arg0, short[] arg1);

	void nglGetPixelMapuiv(int arg0, long arg1);

	void glGetPixelMapuiv(int arg0, int[] arg1);

	void glGetPixelMapuiv(int arg0, long arg1);

	void glGetPixelMapuiv(int arg0, java.nio.IntBuffer arg1);

	void nglGetPointerv(int arg0, long arg1);

	void glGetPointerv(int arg0, org.lwjgl.PointerBuffer arg1);

	long glGetPointer(int arg0);

	void nglGetPolygonStipple(long arg0);

	void glGetPolygonStipple(long arg0);

	void glGetPolygonStipple(java.nio.ByteBuffer arg0);

	void nglGetTexEnviv(int arg0, int arg1, long arg2);

	void glGetTexEnviv(int arg0, int arg1, int[] arg2);

	void glGetTexEnviv(int arg0, int arg1, java.nio.IntBuffer arg2);

	int glGetTexEnvi(int arg0, int arg1);

	void nglGetTexEnvfv(int arg0, int arg1, long arg2);

	void glGetTexEnvfv(int arg0, int arg1, float[] arg2);

	void glGetTexEnvfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	float glGetTexEnvf(int arg0, int arg1);

	void nglGetTexGeniv(int arg0, int arg1, long arg2);

	void glGetTexGeniv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glGetTexGeniv(int arg0, int arg1, int[] arg2);

	int glGetTexGeni(int arg0, int arg1);

	void nglGetTexGenfv(int arg0, int arg1, long arg2);

	void glGetTexGenfv(int arg0, int arg1, float[] arg2);

	void glGetTexGenfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	float glGetTexGenf(int arg0, int arg1);

	void nglGetTexGendv(int arg0, int arg1, long arg2);

	void glGetTexGendv(int arg0, int arg1, double[] arg2);

	void glGetTexGendv(int arg0, int arg1, java.nio.DoubleBuffer arg2);

	double glGetTexGend(int arg0, int arg1);

	void nglGetTexImage(int arg0, int arg1, int arg2, int arg3, long arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, java.nio.ShortBuffer arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, double[] arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, java.nio.FloatBuffer arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, java.nio.DoubleBuffer arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, long arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, java.nio.IntBuffer arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, java.nio.ByteBuffer arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, float[] arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, short[] arg4);

	void glGetTexImage(int arg0, int arg1, int arg2, int arg3, int[] arg4);

	void nglGetTexLevelParameteriv(int arg0, int arg1, int arg2, long arg3);

	void glGetTexLevelParameteriv(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	void glGetTexLevelParameteriv(int arg0, int arg1, int arg2, int[] arg3);

	int glGetTexLevelParameteri(int arg0, int arg1, int arg2);

	void nglGetTexLevelParameterfv(int arg0, int arg1, int arg2, long arg3);

	void glGetTexLevelParameterfv(int arg0, int arg1, int arg2, java.nio.FloatBuffer arg3);

	void glGetTexLevelParameterfv(int arg0, int arg1, int arg2, float[] arg3);

	float glGetTexLevelParameterf(int arg0, int arg1, int arg2);

	void glIndexi(int arg0);

	void glIndexub(byte arg0);

	void glIndexs(short arg0);

	void glIndexf(float arg0);

	void glIndexd(double arg0);

	void nglIndexiv(long arg0);

	void glIndexiv(java.nio.IntBuffer arg0);

	void glIndexiv(int[] arg0);

	void nglIndexubv(long arg0);

	void glIndexubv(java.nio.ByteBuffer arg0);

	void nglIndexsv(long arg0);

	void glIndexsv(java.nio.ShortBuffer arg0);

	void glIndexsv(short[] arg0);

	void nglIndexfv(long arg0);

	void glIndexfv(float[] arg0);

	void glIndexfv(java.nio.FloatBuffer arg0);

	void nglIndexdv(long arg0);

	void glIndexdv(double[] arg0);

	void glIndexdv(java.nio.DoubleBuffer arg0);

	void glIndexMask(int arg0);

	void nglIndexPointer(int arg0, int arg1, long arg2);

	void glIndexPointer(int arg0, int arg1, long arg2);

	void glIndexPointer(int arg0, java.nio.ByteBuffer arg1);

	void glIndexPointer(int arg0, java.nio.IntBuffer arg1);

	void glIndexPointer(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glIndexPointer(int arg0, java.nio.ShortBuffer arg1);

	void glInitNames();

	void nglInterleavedArrays(int arg0, int arg1, long arg2);

	void glInterleavedArrays(int arg0, int arg1, double[] arg2);

	void glInterleavedArrays(int arg0, int arg1, float[] arg2);

	void glInterleavedArrays(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glInterleavedArrays(int arg0, int arg1, int[] arg2);

	void glInterleavedArrays(int arg0, int arg1, short[] arg2);

	void glInterleavedArrays(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glInterleavedArrays(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glInterleavedArrays(int arg0, int arg1, long arg2);

	void glInterleavedArrays(int arg0, int arg1, java.nio.DoubleBuffer arg2);

	void glInterleavedArrays(int arg0, int arg1, java.nio.ShortBuffer arg2);

	boolean glIsList(int arg0);

	void glLightModeli(int arg0, int arg1);

	void glLightModelf(int arg0, float arg1);

	void nglLightModeliv(int arg0, long arg1);

	void glLightModeliv(int arg0, int[] arg1);

	void glLightModeliv(int arg0, java.nio.IntBuffer arg1);

	void nglLightModelfv(int arg0, long arg1);

	void glLightModelfv(int arg0, float[] arg1);

	void glLightModelfv(int arg0, java.nio.FloatBuffer arg1);

	void glLighti(int arg0, int arg1, int arg2);

	void glLightf(int arg0, int arg1, float arg2);

	void nglLightiv(int arg0, int arg1, long arg2);

	void glLightiv(int arg0, int arg1, int[] arg2);

	void glLightiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void nglLightfv(int arg0, int arg1, long arg2);

	void glLightfv(int arg0, int arg1, float[] arg2);

	void glLightfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glLineStipple(int arg0, short arg1);

	void glListBase(int arg0);

	void nglLoadMatrixf(long arg0);

	void glLoadMatrixf(java.nio.FloatBuffer arg0);

	void glLoadMatrixf(float[] arg0);

	void nglLoadMatrixd(long arg0);

	void glLoadMatrixd(java.nio.DoubleBuffer arg0);

	void glLoadMatrixd(double[] arg0);

	void glLoadIdentity();

	void glLoadName(int arg0);

	void glLogicOp(int arg0);

	void nglMap1f(int arg0, float arg1, float arg2, int arg3, int arg4, long arg5);

	void glMap1f(int arg0, float arg1, float arg2, int arg3, int arg4, java.nio.FloatBuffer arg5);

	void glMap1f(int arg0, float arg1, float arg2, int arg3, int arg4, float[] arg5);

	void nglMap1d(int arg0, double arg1, double arg2, int arg3, int arg4, long arg5);

	void glMap1d(int arg0, double arg1, double arg2, int arg3, int arg4, java.nio.DoubleBuffer arg5);

	void glMap1d(int arg0, double arg1, double arg2, int arg3, int arg4, double[] arg5);

	void nglMap2f(int arg0, float arg1, float arg2, int arg3, int arg4, float arg5, float arg6, int arg7, int arg8, long arg9);

	void glMap2f(int arg0, float arg1, float arg2, int arg3, int arg4, float arg5, float arg6, int arg7, int arg8, java.nio.FloatBuffer arg9);

	void glMap2f(int arg0, float arg1, float arg2, int arg3, int arg4, float arg5, float arg6, int arg7, int arg8, float[] arg9);

	void nglMap2d(int arg0, double arg1, double arg2, int arg3, int arg4, double arg5, double arg6, int arg7, int arg8, long arg9);

	void glMap2d(int arg0, double arg1, double arg2, int arg3, int arg4, double arg5, double arg6, int arg7, int arg8, java.nio.DoubleBuffer arg9);

	void glMap2d(int arg0, double arg1, double arg2, int arg3, int arg4, double arg5, double arg6, int arg7, int arg8, double[] arg9);

	void glMapGrid1f(int arg0, float arg1, float arg2);

	void glMapGrid1d(int arg0, double arg1, double arg2);

	void glMapGrid2f(int arg0, float arg1, float arg2, int arg3, float arg4, float arg5);

	void glMapGrid2d(int arg0, double arg1, double arg2, int arg3, double arg4, double arg5);

	void glMateriali(int arg0, int arg1, int arg2);

	void glMaterialf(int arg0, int arg1, float arg2);

	void nglMaterialiv(int arg0, int arg1, long arg2);

	void glMaterialiv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glMaterialiv(int arg0, int arg1, int[] arg2);

	void nglMaterialfv(int arg0, int arg1, long arg2);

	void glMaterialfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glMaterialfv(int arg0, int arg1, float[] arg2);

	void glMatrixMode(int arg0);

	void nglMultMatrixf(long arg0);

	void glMultMatrixf(java.nio.FloatBuffer arg0);

	void glMultMatrixf(float[] arg0);

	void nglMultMatrixd(long arg0);

	void glMultMatrixd(java.nio.DoubleBuffer arg0);

	void glMultMatrixd(double[] arg0);

	void glFrustum(double arg0, double arg1, double arg2, double arg3, double arg4, double arg5);

	void glNewList(int arg0, int arg1);

	void glEndList();

	void glNormal3f(float arg0, float arg1, float arg2);

	void glNormal3b(byte arg0, byte arg1, byte arg2);

	void glNormal3s(short arg0, short arg1, short arg2);

	void glNormal3i(int arg0, int arg1, int arg2);

	void glNormal3d(double arg0, double arg1, double arg2);

	void nglNormal3fv(long arg0);

	void glNormal3fv(java.nio.FloatBuffer arg0);

	void glNormal3fv(float[] arg0);

	void nglNormal3bv(long arg0);

	void glNormal3bv(java.nio.ByteBuffer arg0);

	void nglNormal3sv(long arg0);

	void glNormal3sv(short[] arg0);

	void glNormal3sv(java.nio.ShortBuffer arg0);

	void nglNormal3iv(long arg0);

	void glNormal3iv(int[] arg0);

	void glNormal3iv(java.nio.IntBuffer arg0);

	void nglNormal3dv(long arg0);

	void glNormal3dv(double[] arg0);

	void glNormal3dv(java.nio.DoubleBuffer arg0);

	void nglNormalPointer(int arg0, int arg1, long arg2);

	void glNormalPointer(int arg0, int arg1, java.nio.ShortBuffer arg2);

	void glNormalPointer(int arg0, int arg1, long arg2);

	void glNormalPointer(int arg0, int arg1, java.nio.ByteBuffer arg2);

	void glNormalPointer(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glNormalPointer(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glOrtho(double arg0, double arg1, double arg2, double arg3, double arg4, double arg5);

	void glPassThrough(float arg0);

	void nglPixelMapfv(int arg0, int arg1, long arg2);

	void glPixelMapfv(int arg0, float[] arg1);

	void glPixelMapfv(int arg0, java.nio.FloatBuffer arg1);

	void glPixelMapfv(int arg0, int arg1, long arg2);

	void nglPixelMapusv(int arg0, int arg1, long arg2);

	void glPixelMapusv(int arg0, int arg1, long arg2);

	void glPixelMapusv(int arg0, java.nio.ShortBuffer arg1);

	void glPixelMapusv(int arg0, short[] arg1);

	void nglPixelMapuiv(int arg0, int arg1, long arg2);

	void glPixelMapuiv(int arg0, java.nio.IntBuffer arg1);

	void glPixelMapuiv(int arg0, int[] arg1);

	void glPixelMapuiv(int arg0, int arg1, long arg2);

	void glPixelStoref(int arg0, float arg1);

	void glPixelTransferi(int arg0, int arg1);

	void glPixelTransferf(int arg0, float arg1);

	void glPixelZoom(float arg0, float arg1);

	void glPointSize(float arg0);

	void glPolygonMode(int arg0, int arg1);

	void nglPolygonStipple(long arg0);

	void glPolygonStipple(long arg0);

	void glPolygonStipple(java.nio.ByteBuffer arg0);

	void glPushAttrib(int arg0);

	void glPushClientAttrib(int arg0);

	void glPopAttrib();

	void glPopClientAttrib();

	void glPopMatrix();

	void glPopName();

	void nglPrioritizeTextures(int arg0, long arg1, long arg2);

	void glPrioritizeTextures(int[] arg0, float[] arg1);

	void glPrioritizeTextures(java.nio.IntBuffer arg0, java.nio.FloatBuffer arg1);

	void glPushMatrix();

	void glPushName(int arg0);

	void glRasterPos2i(int arg0, int arg1);

	void glRasterPos2s(short arg0, short arg1);

	void glRasterPos2f(float arg0, float arg1);

	void glRasterPos2d(double arg0, double arg1);

	void nglRasterPos2iv(long arg0);

	void glRasterPos2iv(java.nio.IntBuffer arg0);

	void glRasterPos2iv(int[] arg0);

	void nglRasterPos2sv(long arg0);

	void glRasterPos2sv(short[] arg0);

	void glRasterPos2sv(java.nio.ShortBuffer arg0);

	void nglRasterPos2fv(long arg0);

	void glRasterPos2fv(java.nio.FloatBuffer arg0);

	void glRasterPos2fv(float[] arg0);

	void nglRasterPos2dv(long arg0);

	void glRasterPos2dv(double[] arg0);

	void glRasterPos2dv(java.nio.DoubleBuffer arg0);

	void glRasterPos3i(int arg0, int arg1, int arg2);

	void glRasterPos3s(short arg0, short arg1, short arg2);

	void glRasterPos3f(float arg0, float arg1, float arg2);

	void glRasterPos3d(double arg0, double arg1, double arg2);

	void nglRasterPos3iv(long arg0);

	void glRasterPos3iv(java.nio.IntBuffer arg0);

	void glRasterPos3iv(int[] arg0);

	void nglRasterPos3sv(long arg0);

	void glRasterPos3sv(java.nio.ShortBuffer arg0);

	void glRasterPos3sv(short[] arg0);

	void nglRasterPos3fv(long arg0);

	void glRasterPos3fv(float[] arg0);

	void glRasterPos3fv(java.nio.FloatBuffer arg0);

	void nglRasterPos3dv(long arg0);

	void glRasterPos3dv(java.nio.DoubleBuffer arg0);

	void glRasterPos3dv(double[] arg0);

	void glRasterPos4i(int arg0, int arg1, int arg2, int arg3);

	void glRasterPos4s(short arg0, short arg1, short arg2, short arg3);

	void glRasterPos4f(float arg0, float arg1, float arg2, float arg3);

	void glRasterPos4d(double arg0, double arg1, double arg2, double arg3);

	void nglRasterPos4iv(long arg0);

	void glRasterPos4iv(int[] arg0);

	void glRasterPos4iv(java.nio.IntBuffer arg0);

	void nglRasterPos4sv(long arg0);

	void glRasterPos4sv(short[] arg0);

	void glRasterPos4sv(java.nio.ShortBuffer arg0);

	void nglRasterPos4fv(long arg0);

	void glRasterPos4fv(java.nio.FloatBuffer arg0);

	void glRasterPos4fv(float[] arg0);

	void nglRasterPos4dv(long arg0);

	void glRasterPos4dv(java.nio.DoubleBuffer arg0);

	void glRasterPos4dv(double[] arg0);

	void glRecti(int arg0, int arg1, int arg2, int arg3);

	void glRects(short arg0, short arg1, short arg2, short arg3);

	void glRectf(float arg0, float arg1, float arg2, float arg3);

	void glRectd(double arg0, double arg1, double arg2, double arg3);

	void nglRectiv(long arg0, long arg1);

	void glRectiv(int[] arg0, int[] arg1);

	void glRectiv(java.nio.IntBuffer arg0, java.nio.IntBuffer arg1);

	void nglRectsv(long arg0, long arg1);

	void glRectsv(short[] arg0, short[] arg1);

	void glRectsv(java.nio.ShortBuffer arg0, java.nio.ShortBuffer arg1);

	void nglRectfv(long arg0, long arg1);

	void glRectfv(java.nio.FloatBuffer arg0, java.nio.FloatBuffer arg1);

	void glRectfv(float[] arg0, float[] arg1);

	void nglRectdv(long arg0, long arg1);

	void glRectdv(java.nio.DoubleBuffer arg0, java.nio.DoubleBuffer arg1);

	void glRectdv(double[] arg0, double[] arg1);

	int glRenderMode(int arg0);

	void glRotatef(float arg0, float arg1, float arg2, float arg3);

	void glRotated(double arg0, double arg1, double arg2, double arg3);

	void glScalef(float arg0, float arg1, float arg2);

	void glScaled(double arg0, double arg1, double arg2);

	void nglSelectBuffer(int arg0, long arg1);

	void glSelectBuffer(java.nio.IntBuffer arg0);

	void glSelectBuffer(int[] arg0);

	void glShadeModel(int arg0);

	void glTexCoord1f(float arg0);

	void glTexCoord1s(short arg0);

	void glTexCoord1i(int arg0);

	void glTexCoord1d(double arg0);

	void nglTexCoord1fv(long arg0);

	void glTexCoord1fv(float[] arg0);

	void glTexCoord1fv(java.nio.FloatBuffer arg0);

	void nglTexCoord1sv(long arg0);

	void glTexCoord1sv(java.nio.ShortBuffer arg0);

	void glTexCoord1sv(short[] arg0);

	void nglTexCoord1iv(long arg0);

	void glTexCoord1iv(java.nio.IntBuffer arg0);

	void glTexCoord1iv(int[] arg0);

	void nglTexCoord1dv(long arg0);

	void glTexCoord1dv(java.nio.DoubleBuffer arg0);

	void glTexCoord1dv(double[] arg0);

	void glTexCoord2f(float arg0, float arg1);

	void glTexCoord2s(short arg0, short arg1);

	void glTexCoord2i(int arg0, int arg1);

	void glTexCoord2d(double arg0, double arg1);

	void nglTexCoord2fv(long arg0);

	void glTexCoord2fv(float[] arg0);

	void glTexCoord2fv(java.nio.FloatBuffer arg0);

	void nglTexCoord2sv(long arg0);

	void glTexCoord2sv(short[] arg0);

	void glTexCoord2sv(java.nio.ShortBuffer arg0);

	void nglTexCoord2iv(long arg0);

	void glTexCoord2iv(java.nio.IntBuffer arg0);

	void glTexCoord2iv(int[] arg0);

	void nglTexCoord2dv(long arg0);

	void glTexCoord2dv(java.nio.DoubleBuffer arg0);

	void glTexCoord2dv(double[] arg0);

	void glTexCoord3f(float arg0, float arg1, float arg2);

	void glTexCoord3s(short arg0, short arg1, short arg2);

	void glTexCoord3i(int arg0, int arg1, int arg2);

	void glTexCoord3d(double arg0, double arg1, double arg2);

	void nglTexCoord3fv(long arg0);

	void glTexCoord3fv(float[] arg0);

	void glTexCoord3fv(java.nio.FloatBuffer arg0);

	void nglTexCoord3sv(long arg0);

	void glTexCoord3sv(java.nio.ShortBuffer arg0);

	void glTexCoord3sv(short[] arg0);

	void nglTexCoord3iv(long arg0);

	void glTexCoord3iv(int[] arg0);

	void glTexCoord3iv(java.nio.IntBuffer arg0);

	void nglTexCoord3dv(long arg0);

	void glTexCoord3dv(double[] arg0);

	void glTexCoord3dv(java.nio.DoubleBuffer arg0);

	void glTexCoord4f(float arg0, float arg1, float arg2, float arg3);

	void glTexCoord4s(short arg0, short arg1, short arg2, short arg3);

	void glTexCoord4i(int arg0, int arg1, int arg2, int arg3);

	void glTexCoord4d(double arg0, double arg1, double arg2, double arg3);

	void nglTexCoord4fv(long arg0);

	void glTexCoord4fv(float[] arg0);

	void glTexCoord4fv(java.nio.FloatBuffer arg0);

	void nglTexCoord4sv(long arg0);

	void glTexCoord4sv(short[] arg0);

	void glTexCoord4sv(java.nio.ShortBuffer arg0);

	void nglTexCoord4iv(long arg0);

	void glTexCoord4iv(java.nio.IntBuffer arg0);

	void glTexCoord4iv(int[] arg0);

	void nglTexCoord4dv(long arg0);

	void glTexCoord4dv(double[] arg0);

	void glTexCoord4dv(java.nio.DoubleBuffer arg0);

	void nglTexCoordPointer(int arg0, int arg1, int arg2, long arg3);

	void glTexCoordPointer(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);

	void glTexCoordPointer(int arg0, int arg1, int arg2, java.nio.ByteBuffer arg3);

	void glTexCoordPointer(int arg0, int arg1, int arg2, long arg3);

	void glTexCoordPointer(int arg0, int arg1, int arg2, java.nio.ShortBuffer arg3);

	void glTexCoordPointer(int arg0, int arg1, int arg2, java.nio.FloatBuffer arg3);

	void glTexEnvi(int arg0, int arg1, int arg2);

	void nglTexEnviv(int arg0, int arg1, long arg2);

	void glTexEnviv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glTexEnviv(int arg0, int arg1, int[] arg2);

	void glTexEnvf(int arg0, int arg1, float arg2);

	void nglTexEnvfv(int arg0, int arg1, long arg2);

	void glTexEnvfv(int arg0, int arg1, float[] arg2);

	void glTexEnvfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glTexGeni(int arg0, int arg1, int arg2);

	void nglTexGeniv(int arg0, int arg1, long arg2);

	void glTexGeniv(int arg0, int arg1, java.nio.IntBuffer arg2);

	void glTexGeniv(int arg0, int arg1, int[] arg2);

	void glTexGenf(int arg0, int arg1, float arg2);

	void nglTexGenfv(int arg0, int arg1, long arg2);

	void glTexGenfv(int arg0, int arg1, float[] arg2);

	void glTexGenfv(int arg0, int arg1, java.nio.FloatBuffer arg2);

	void glTexGend(int arg0, int arg1, double arg2);

	void nglTexGendv(int arg0, int arg1, long arg2);

	void glTexGendv(int arg0, int arg1, double[] arg2);

	void glTexGendv(int arg0, int arg1, java.nio.DoubleBuffer arg2);

	void nglTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, long arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, double[] arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, float[] arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, java.nio.FloatBuffer arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, java.nio.IntBuffer arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int[] arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, short[] arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, java.nio.DoubleBuffer arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, java.nio.ByteBuffer arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, long arg7);

	void glTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, java.nio.ShortBuffer arg7);

	void glCopyTexImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6);

	void glCopyTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5);

	void nglTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.ShortBuffer arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.IntBuffer arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.FloatBuffer arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.DoubleBuffer arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, double[] arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, float[] arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int[] arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, short[] arg6);

	void glTexSubImage1D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.ByteBuffer arg6);

	void glTranslatef(float arg0, float arg1, float arg2);

	void glTranslated(double arg0, double arg1, double arg2);

	void glVertex2f(float arg0, float arg1);

	void glVertex2s(short arg0, short arg1);

	void glVertex2i(int arg0, int arg1);

	void glVertex2d(double arg0, double arg1);

	void nglVertex2fv(long arg0);

	void glVertex2fv(float[] arg0);

	void glVertex2fv(java.nio.FloatBuffer arg0);

	void nglVertex2sv(long arg0);

	void glVertex2sv(short[] arg0);

	void glVertex2sv(java.nio.ShortBuffer arg0);

	void nglVertex2iv(long arg0);

	void glVertex2iv(int[] arg0);

	void glVertex2iv(java.nio.IntBuffer arg0);

	void nglVertex2dv(long arg0);

	void glVertex2dv(java.nio.DoubleBuffer arg0);

	void glVertex2dv(double[] arg0);

	void glVertex3f(float arg0, float arg1, float arg2);

	void glVertex3s(short arg0, short arg1, short arg2);

	void glVertex3i(int arg0, int arg1, int arg2);

	void glVertex3d(double arg0, double arg1, double arg2);

	void nglVertex3fv(long arg0);

	void glVertex3fv(float[] arg0);

	void glVertex3fv(java.nio.FloatBuffer arg0);

	void nglVertex3sv(long arg0);

	void glVertex3sv(short[] arg0);

	void glVertex3sv(java.nio.ShortBuffer arg0);

	void nglVertex3iv(long arg0);

	void glVertex3iv(int[] arg0);

	void glVertex3iv(java.nio.IntBuffer arg0);

	void nglVertex3dv(long arg0);

	void glVertex3dv(java.nio.DoubleBuffer arg0);

	void glVertex3dv(double[] arg0);

	void glVertex4f(float arg0, float arg1, float arg2, float arg3);

	void glVertex4s(short arg0, short arg1, short arg2, short arg3);

	void glVertex4i(int arg0, int arg1, int arg2, int arg3);

	void glVertex4d(double arg0, double arg1, double arg2, double arg3);

	void nglVertex4fv(long arg0);

	void glVertex4fv(java.nio.FloatBuffer arg0);

	void glVertex4fv(float[] arg0);

	void nglVertex4sv(long arg0);

	void glVertex4sv(java.nio.ShortBuffer arg0);

	void glVertex4sv(short[] arg0);

	void nglVertex4iv(long arg0);

	void glVertex4iv(int[] arg0);

	void glVertex4iv(java.nio.IntBuffer arg0);

	void nglVertex4dv(long arg0);

	void glVertex4dv(java.nio.DoubleBuffer arg0);

	void glVertex4dv(double[] arg0);

	void nglVertexPointer(int arg0, int arg1, int arg2, long arg3);

	void glVertexPointer(int arg0, int arg1, int arg2, java.nio.FloatBuffer arg3);

	void glVertexPointer(int arg0, int arg1, int arg2, long arg3);

	void glVertexPointer(int arg0, int arg1, int arg2, java.nio.ByteBuffer arg3);

	void glVertexPointer(int arg0, int arg1, int arg2, java.nio.ShortBuffer arg3);

	void glVertexPointer(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3);
}
