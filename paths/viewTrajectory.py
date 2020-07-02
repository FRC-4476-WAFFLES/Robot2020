#!/usr/bin/env python3

# Example line: State(Sec: 1.27, Vel m/s: 1.91, Accel m/s/s: 1.50, Pose: Pose2d(Translation2d(X: 4.02, Y: 0.62), Rotation2d(Rads: 0.77, Deg: 44.34)), Curvature: 0.49),

import argparse
import re
import pprint
import matplotlib.pyplot as plt

arg_parser = argparse.ArgumentParser(description='Converts a text description of a path to a graphic showing it')
arg_parser.add_argument('input', help='The path to the input string (toString() of a Trajectory Object).')
args = arg_parser.parse_args()

def parse_line(x):
    match = re.match(
        "State\\(Sec: (?P<Sec>-?\\d+\\.\\d+)"
        ", Vel m/s: (?P<Vel>-?\\d+\\.\\d+)"
        ", Accel m/s/s: (?P<Accel>-?\\d+\\.\\d+)"
        ", Pose: Pose2d\\(Translation2d\\(X: (?P<X>-?\\d+\\.\\d+)"
        ", Y: (?P<Y>-?\\d+\\.\\d+)\\)"
        ", Rotation2d\\(Rads: (?P<Rads>-?\\d+\\.\\d+)"
        ", Deg: (?P<Deg>-?\\d+\\.\\d+)\\)\\)"
        ", Curvature: (?P<Curvature>-?\\d+\\.\\d+)\\)", x)
    if match:
        return {k: float(v) for k, v in match.groupdict().items()}
    else:
        print("Error on " + x)

with open(args.input) as fd:
    parsed = list(map(parse_line, fd.read().splitlines()))
    xs = [v['X'] for v in parsed]
    ys = [v['Y'] for v in parsed]
    plt.scatter(xs, ys)
    plt.show()
