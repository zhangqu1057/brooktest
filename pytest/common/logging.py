import logging

class Logger(object):
    """Generate conversion logs to both File and Console"""
    def __init__(self):
        log_path = "C:\ws\python.log"
        self.logger = logging.getLogger()
        self.logger.setLevel(logging.DEBUG)

        # Write the logs into a file
        fh = logging.FileHandler(log_path, mode = 'a')
        fh.setLevel(logging.DEBUG)

        # Output logs to console
        ch = logging.StreamHandler()
        ch.setLevel(logging.DEBUG)

        log_format = logging.Formatter("%(asctime)s [%(levelname)s] %(message)s")
        fh.setFormatter(log_format)
        ch.setFormatter(log_format)

        self.logger.addHandler(fh)
        self.logger.addHandler(ch)

    def getLogger(self):
        return self.logger