package lu.pcy113.pdr.engine.utils.interpol;

import org.joml.Math;

public enum Interpolators
		implements
		Interpolator {

	LINEAR {
		@Override
		public float evaluate(float x) {
			return Math.clamp(0, 1, x);
		}
	},

	QUAD_IN {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return x * x;
		}
	},

	QUAD_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return 1 - (1 - x) * (1 - x);
		}
	},

	QUAD_IN_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return x < 0.5 ? 2 * x * x : 1 - 2 * (1 - x) * (1 - x);
		}
	},

	CUBIC_IN {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return x * x * x;
		}
	},

	CUBIC_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return 1 - (1 - x) * (1 - x) * (1 - x);
		}
	},

	CUBIC_IN_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return x < 0.5 ? 4 * x * x * x : 1 - 4 * (1 - x) * (1 - x) * (1 - x);
		}
	},

	SINE_IN_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return (float) (-0.5 * (Math.cos(Math.PI * x) - 1));
		}
	},
	SINE_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return (float) (Math.sin((x * Math.PI) / 2));
		}
	},
	SINE_IN {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return (float) (1 - Math.cos((x * Math.PI) / 2));
		}
	},

	QUART_IN {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return x * x * x * x;
		}
	},

	QUART_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return 1 - (1 - x) * (1 - x) * (1 - x) * (1 - x);
		}
	},

	QUART_IN_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return x < 0.5 ? 8 * x * x * x * x : 1 - 8 * (1 - x) * (1 - x) * (1 - x) * (1 - x);
		}
	},

	QUINT_IN {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return x * x * x * x * x;
		}
	},

	QUINT_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return 1 - (1 - x) * (1 - x) * (1 - x) * (1 - x) * (1 - x);
		}
	},

	QUINT_IN_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return x < 0.5 ? 16 * x * x * x * x * x : 1 - 16 * (1 - x) * (1 - x) * (1 - x) * (1 - x) * (1 - x);
		}
	},

	EXPO_IN {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return (float) java.lang.Math.pow(2, 10 * (x - 1));
		}
	},

	EXPO_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return 1 - (float) java.lang.Math.pow(2, -10 * x);
		}
	},

	EXPO_IN_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			if (x < 0.5) {
				return (float) (0.5 * java.lang.Math.pow(2, 10 * (2 * x - 1)));
			} else {
				return (float) (-0.5 * java.lang.Math.pow(2, -10 * (2 * x - 1)) + 1);
			}
		}
	},

	CIRC_IN {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return 1 - Math.sqrt(1 - x * x);
		}
	},

	CIRC_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			return Math.sqrt(1 - (x - 1) * (x - 1));
		}
	},

	CIRC_IN_OUT {
		@Override
		public float evaluate(float x) {
			x = Math.clamp(0, 1, x);
			if (x < 0.5) {
				return (float) (0.5 * (1 - Math.sqrt(1 - 4 * x * x)));
			} else {
				return (float) (0.5 * (Math.sqrt(1 - (2 * x - 2) * (2 * x - 2)) + 1));
			}
		}
	};

}
