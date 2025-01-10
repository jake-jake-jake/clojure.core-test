(ns clojure.core-test.quot
  (:require [clojure.test :as t :refer [deftest testing is are]]
            [clojure.core-test.portability :as p]))

(deftest test-quot
  (are [type-pred expected x y] (let [r (quot x y)]
                                  (and (type-pred r)
                                       (= expected r)))
    int? 3  10  3
    int? -3 -10 3
    int? 3  -10 -3
    int? -3 10  -3

    p/big-int? 3N  10   3N
    p/big-int? -3N -10  3N
    p/big-int? 3N  -10  -3N
    p/big-int? -3N 10   -3N
    p/big-int? 3N  10N  3
    p/big-int? -3N -10N 3
    p/big-int? 3N  -10N -3
    p/big-int? -3N 10N  -3
    p/big-int? 3N  10N  3N
    p/big-int? -3N -10N 3N
    p/big-int? 3N  -10N -3N
    p/big-int? -3N 10N  -3N

    double? 3.0  10    3.0
    double? -3.0 -10   3.0
    double? 3.0  -10   -3.0
    double? -3.0 10    -3.0
    double? 3.0  10.0  3
    double? -3.0 -10.0 3
    double? 3.0  -10.0 -3
    double? -3.0 10.0  -3
    double? 3.0  10.0  3.0
    double? -3.0 -10.0 3.0
    double? 3.0  -10.0 -3.0
    double? -3.0 10.0  -3.0

    decimal? 3.0M  10     3.0M
    decimal? -3.0M -10    3.0M
    decimal? 3.0M  -10    -3.0M
    decimal? -3.0M 10     -3.0M
    decimal? 3.0M  10.0M  3
    decimal? -3.0M -10.0M 3
    decimal? 3.0M  -10.0M -3
    decimal? -3.0M 10.0M  -3
    decimal? 3.0M  10.0M  3.0M
    decimal? -3.0M -10.0M 3.0M
    decimal? 3.0M  -10.0M -3.0M
    decimal? -3.0M 10.0M  -3.0M

    ;; Unexpectedly downconverts result to double, rather than BigDecimal
    double? 3.0  10.0M  3.0
    double? -3.0 -10.0M 3.0
    double? 3.0  -10.0M -3.0
    double? -3.0 10.0M  -3.0
    double? 3.0  10.0   3.0M
    double? -3.0 -10.0  3.0M
    double? 3.0  -10.0  -3.0M
    double? -3.0 10.0   -3.0M

    ;; Anything with a ratio seems to convert to BigInt
    p/big-int? 6N  3     1/2
    p/big-int? 2N  3     4/3
    p/big-int? 1N  37/2  15
    p/big-int? -6N 3     -1/2
    p/big-int? -2N 3     -4/3
    p/big-int? -1N 37/2  -15
    p/big-int? -6N -3    1/2
    p/big-int? -2N -3    4/3
    p/big-int? -1N -37/2 15
    p/big-int? 6N  -3    -1/2
    p/big-int? 2N  -3    -4/3
    p/big-int? 1N  -37/2 -15

    double? 0.0 1 ##Inf
    double? 0.0 1 ##-Inf)

  (is (thrown? Exception (quot 10 0)))
  (is (thrown? Exception (quot ##Inf 1))) ; surprising since (/ ##Inf 1) = ##Inf
  (is (thrown? Exception (quot ##-Inf 1))) ; surprising since (/ ##-Inf 1) = ##-Inf
  (is (thrown? Exception (quot ##NaN 1)))
  (is (thrown? Exception (quot 1 ##NaN)))
  (is (thrown? Exception (quot ##NaN 1))))
