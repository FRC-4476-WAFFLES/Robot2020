{
    # Class names of motor controllers used.
    # Options:
    # 'WPI_TalonSRX'
    # 'WPI_TalonFX' (for Falcon 500 motors)
    # 'WPI_VictorSPX'
    # Note: The first motor on each side should always be a Talon SRX/FX, as the
    # VictorSPX does not support encoder connections
    "rightControllerTypes": ["WPI_TalonFX", "WPI_TalonFX"],
    "leftControllerTypes": ["WPI_TalonFX", "WPI_TalonFX"],

    # Note: The first id in the list of ports should be the one with an encoder
    # Ports for the left-side motors
    "leftMotorPorts": [1, 2],
    # Ports for the right-side motors
    "rightMotorPorts": [3, 4],

    # Inversions for the left-side motors
    "leftMotorsInverted": [False, False],
    # Inversions for the right side motors
    "rightMotorsInverted": [False, False],

    # Wheel diameter (in units of your choice - will dictate units of analysis)
    "wheelDiameter": 6.0 * 0.0254, # 6 inches in metres

    # If your robot has only one encoder, set all right encoder fields to `None`
    # Encoder edges-per-revolution (*NOT* cycles per revolution!)
    # This value should be the edges per revolution *of the wheels*, and so
    # should take into account gearing between the encoder and the wheels
    
    # 2048 is ticks per rotation on falcons
    # Drive gears are 50:14 and 54:20
    "encoderEPR": 2048.0 * (50.0/14.0) * (54.0/20.0),
	

    # Whether the left encoder is inverted
    "leftEncoderInverted": False,
    # Whether the right encoder is inverted:
    "rightEncoderInverted": True,

    # Your gyro type (one of "NavX", "Pigeon", "ADXRS450", "AnalogGyro", or "None")
    "gyroType": "ADXRS450",
    # Whatever you put into the constructor of your gyro
    # Could be:
    # "SPI.Port.kMXP" (MXP SPI port for NavX or ADXRS450),
    # "SerialPort.Port.kMXP" (MXP Serial port for NavX),
    # "I2C.Port.kOnboard" (Onboard I2C port for NavX),
    # "0" (Pigeon CAN ID or AnalogGyro channel),
    # "new WPI_TalonSRX(3)" (Pigeon on a Talon SRX),
    # "leftSlave" (Pigeon on the left slave Talon SRX/FX),
    # "" (NavX using default SPI, ADXRS450 using onboard CS0, or no gyro)
    "gyroPort": "",
}




