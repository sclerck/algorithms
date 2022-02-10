# Perlin Noise and Midpoint Displacement Algorithms
A GRPC server that publishes a stream of timestamped floats (aka ticks) given the following inputs:
* The algorithm to use 
    * MIDPOINT_DISPLACEMENT
    * PERLIN_NOISE
* The volatility (STABLE, NORMAL, ABNORMAL)
    * Volatility refers to how severe the peaks and troughs are in the data
* The number of ticks to receive
* The seed (i.e. the starting value)

The server implementation is in Java and the client is also in Java although since this is proto, any applicable client can be used.

