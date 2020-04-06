import os
import common.logging


def main():
    log = common.logging.Logger().getLogger()
    log.debug(os.name)


if __name__ == '__main__':
    main()