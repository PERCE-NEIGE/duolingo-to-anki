package net.abesto.duolingotoanki.exporters

import java.io.{File, PrintWriter}

import net.abesto.duolingotoanki.{Log, Word}

class AnkiExporter(f: File, debug: Boolean) {
  def write(words: Seq[Word]): Either[String, Unit] = {
    try {
      val pw = new PrintWriter(f, "UTF-8")
      try {
        if (debug) {
          pw.write("#### Start of scraping log\n")
          for (logline <- Log.lines) pw.write("# " + logline + "\n")
          pw.write("#### End of scraping log, data coming up\n")
        }
        for (word <- words) {
          pw.write(
            Seq(word.learned, word.native)
              .map(quote("\n"))
              .:+(quote(" ")(word.tags))
              .mkString(";")
              .++("\n\n")
          )
        }
        Right(Unit)
      } finally {
        pw.close()
      }
    } catch {
      case e: Throwable => Left(e.toString)
    }
  }

  def quote(sep: String)(s: Seq[String]) = '"' + s.mkString(sep) + '"'
}
